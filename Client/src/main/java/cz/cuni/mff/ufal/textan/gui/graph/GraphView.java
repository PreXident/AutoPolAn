package cz.cuni.mff.ufal.textan.gui.graph;

import cz.cuni.mff.ufal.textan.commons.utils.Triple;
import cz.cuni.mff.ufal.textan.core.Object;
import cz.cuni.mff.ufal.textan.core.Relation;
import cz.cuni.mff.ufal.textan.gui.TextAnController;
import cz.cuni.mff.ufal.textan.gui.Utils;
import cz.cuni.mff.ufal.textan.gui.Utils.IdType;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.visualization.Layer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import edu.uci.ics.jung.visualization.transform.MutableTransformer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.embed.swing.SwingNode;
import javafx.scene.control.ContextMenu;
import javax.swing.SwingUtilities;
import org.apache.commons.collections15.Transformer;

/**
 * Displays graphs.
 */
public class GraphView extends SwingNode {

    /** Color for vertices representing relations. */
    final Color RELATION_OBJECT_COLOR = new Color(238, 238, 238);

    /** Color for vetex border. */
    final Color VERTEX_BORDER_COLOR = new Color(0, 0, 0, 0);

    /**
     * Properties containing application settings.
     * Handle with care, they are shared!
     */
    final Properties settings;

    /** Graph visualizator. */
    final VisualizationViewer<Object, Relation> visualizator;

    /** Object context menu. */
    ContextMenu objectContextMenu;

    /** Relation context menu. */
    ContextMenu relationContextMenu;

    /** Object to display graph for. */
    final ObjectProperty<Object> objectForGraph = new SimpleObjectProperty<>();

    /** Relation to display graph for. */
    final ObjectProperty<Relation> relationForGraph = new SimpleObjectProperty<>();

    /** Central object. */
    Object centerObject;

    /** Central relation. */
    Relation centerRelation;

    /** Vizualizator's layout. */
    Layout<Object, Relation> layout;

    /** Mouse handler. */
    final DefaultModalGraphMouse<Integer,String> graphMouse;

    /**
     * Only constructor.
     * @param settings application settings
     * @param objects graph verteces
     * @param relations graph edges
     * @param centerer predicate deciding the center of the graph
     */
    public GraphView(final Properties settings, final Map<Long, Object> objects,
            final Set<Relation> relations, final Predicate<java.lang.Object> centerer) {
        this.settings = settings;
        final String providerId = settings.getProperty(TextAnController.HYPER_GRAPHS, "JUNGHyperGraphProvider");
        final IHyperGraphProvider provider =
                HyperGraphService.getInstance().getProvider(providerId);
        final Hypergraph<Object, Relation> g = provider.createHyperGraph();
        // Add vertices
        provider.addObjects(g, objects.values());
        // Add edges
        provider.addRelations(g, relations);
        // find center object
        for (Object obj : g.getVertices()) {
            if (centerer.test(obj)) {
                centerObject = obj;
            }
        }
        // find center relation
        for (Relation rel : g.getEdges()) {
            if (centerer.test(rel)) {
                centerRelation = rel;
            }
        }
        //
        layout = new FRLayout<>(provider.transformForLayout(g));
        //
        final Transformer<Object, Paint> vertexPaint = obj -> {
            final IdType type = obj instanceof RelationObject? IdType.RELATION : IdType.ENTITY;
            return Utils.resolveColorAWT(settings, type, obj.getType().getId());
        };
        float dash[] = {10.0f};
        final Stroke edgeStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        Transformer<Relation, Stroke> edgeStrokeTransformer = (Relation s) -> edgeStroke;
        //
        visualizator = new VisualizationViewer<>(layout);
        final Renderer<Object, Relation> renderer = provider.createRenderer();
        if (renderer != null) {
            visualizator.setRenderer(renderer);
        }
        //
        visualizator.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        visualizator.getRenderContext().setVertexDrawPaintTransformer(obj -> VERTEX_BORDER_COLOR);
        visualizator.getRenderContext().setVertexShapeTransformer(v -> {
            return v instanceof RelationObject ? new Rectangle(-10, -10, 20, 20) : new Ellipse2D.Float(-10, -10, 20, 20);
        });
        visualizator.getRenderContext().setEdgeStrokeTransformer(edgeStrokeTransformer);
        visualizator.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
        visualizator.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<>());
        visualizator.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);
        // Create a graph mouse and add it to the visualization component
        graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        graphMouse.add(new AbstractPopupGraphMousePlugin() {

            @Override
            public void mousePressed(MouseEvent e) {
                if (objectContextMenu != null && objectContextMenu.isShowing()) {
                    Platform.runLater(() -> {
                        objectContextMenu.hide();
                    });
                }
                if (relationContextMenu != null && relationContextMenu.isShowing()) {
                    Platform.runLater(() -> {
                        relationContextMenu.hide();
                    });
                }
                super.mousePressed(e);
            }

            @Override
            protected void handlePopup(MouseEvent e) {
                @SuppressWarnings("unchecked")
                final VisualizationViewer<Object, Relation> vv =
                        (VisualizationViewer<Object, Relation>) e.getSource();
                final Point2D p = e.getPoint();

                final GraphElementAccessor<Object, Relation> pickSupport = vv.getPickSupport();
                if(pickSupport != null) {
                    final Point s = MouseInfo.getPointerInfo().getLocation(); //e.getLocationOnScreen() is not good enough
                    final Object v = pickSupport.getVertex(vv.getGraphLayout(), p.getX(), p.getY());
                    Relation edge = pickSupport.getEdge(vv.getGraphLayout(), p.getX(), p.getY());
                    if (v != null) {
                        if (v instanceof RelationObject) {
                            edge = ((RelationObject) v).getRelation();
                        } else if (objectContextMenu != null) {
                            Platform.runLater(() -> {
                                objectForGraph.set(v);
                                objectContextMenu.show(GraphView.this, s.getX(), s.getY());
                            });
                            return;
                        }
                    }
                    final Relation rel = edge;
                    if (rel != null && relationContextMenu != null) {
                        Platform.runLater(() -> {
                            relationForGraph.set(rel);
                            relationContextMenu.show(GraphView.this, s.getX(), s.getY());
                        });
                    }
                }
            }
        });
        visualizator.setGraphMouse(graphMouse);
        visualizator.addKeyListener(new DefaultModalGraphMouse.ModeKeyAdapter(graphMouse)); //press t and p to change modes!
        //
        try {
            SwingUtilities.invokeAndWait(() -> {
                this.setContent(visualizator);
            });
        } catch (Exception e) { }

        //center to the graph root, for some reason we must wait a bit
        new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (Exception e) { }
            home();
        }).start();
    }

    /**
     * Returns context menu for nodes.
     * @return context menu for nodes
     */
    public ContextMenu getObjectContextMenu() {
        return objectContextMenu;
    }

    /**
     * Sets context menu for nodes.
     * @param objectContextMenu new object context menu
     */
    public void setObjectContextMenu(final ContextMenu objectContextMenu) {
        this.objectContextMenu = objectContextMenu;
    }

    /**
     * Returns context menu for edges.
     * @return context menu for edges
     */
    public ContextMenu getRelationContextMenu() {
        return relationContextMenu;
    }

    /**
     * Sets context menu for edges.
     * @param relationContextMenu new relation context menu
     */
    public void setRelationContextMenu(final ContextMenu relationContextMenu) {
        this.relationContextMenu = relationContextMenu;
    }

    /**
     * Centers to the given object
     * @param object new center
     */
    public void centerToObject(final Object object) {
       SwingUtilities.invokeLater(() -> {
            final Point2D p = layout.transform(object);
            final MutableTransformer layout2 = visualizator.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
            Point2D ctr = visualizator.getCenter();
            final double scale = visualizator.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getScale();
            ctr = visualizator.getRenderContext().getMultiLayerTransformer().inverseTransform(ctr);
            final double deltaX = (ctr.getX() - p.getX())*1/scale;
            final double deltaY = (ctr.getY() - p.getY())*1/scale;
            layout2.translate(deltaX, deltaY);
            visualizator.repaint();
        });
    }

    /**
     * Centers to the given relation
     * @param relation new center
     */
    public void centerToRelation(final Relation relation) {
        SwingUtilities.invokeLater(() -> {
            final List<Object> objects = relation.getObjects().stream()
                    .map(Triple<Integer, String, Object>::getThird)
                    .collect(Collectors.toList());
            //TODO what to do with hyperedges? - That's easy: calculate center of gravity for convex hull of nodes...
            if (objects.size() != 2) {
                return;
            }
            final Point2D p1 = layout.transform(objects.get(0));
            final Point2D p2 = layout.transform(objects.get(1));
            final Point2D p = new Point2D.Double(
                    (p1.getX() + p2.getX()) / 2,
                    (p1.getY() + p2.getY()) / 2
            );
            final MutableTransformer layout2 = visualizator.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.LAYOUT);
            Point2D ctr = visualizator.getCenter();
            final double scale = visualizator.getRenderContext().getMultiLayerTransformer().getTransformer(Layer.VIEW).getScale();
            ctr = visualizator.getRenderContext().getMultiLayerTransformer().inverseTransform(ctr);
            final double deltaX = (ctr.getX() - p.getX())*1/scale;
            final double deltaY = (ctr.getY() - p.getY())*1/scale;
            layout2.translate(deltaX, deltaY);
            visualizator.repaint();
       });
    }

    /**
     * Center to the graph root.
     */
    public void home() {
        if (centerObject != null) {
            centerToObject(centerObject);
        } else if (centerRelation != null) {
            centerToRelation(centerRelation);
        }
    }

    /**
     * Switches graph to pick mode.
     */
    public void pick() {
        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
    }

    @Override
    public void resize(double width, double height) {
        super.resize(width, height);
        SwingUtilities.invokeLater(() -> {
            visualizator.setSize((int) width, (int) height);
        });
    }

    /**
     * Switches graph to transform mode.
     */
    public void transform() {
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
    }
}
