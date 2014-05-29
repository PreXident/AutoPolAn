package cz.cuni.mff.ufal.textan.core.graph;

import cz.cuni.mff.ufal.textan.core.Client;
import cz.cuni.mff.ufal.textan.core.Graph;
import cz.cuni.mff.ufal.textan.core.IdNotFoundException;
import cz.cuni.mff.ufal.textan.core.Object;
import java.util.List;
import java.util.Set;

/**
 * Provides information about graphs.
 */
public class Grapher {

    /** Distance from root to fetch. */
    protected int distance = -1;

    /** Root central object id. */
    protected long rootId = -1;

    /** Client connecting to webservices. */
    final protected Client client;

    /**
     * Only constructor.
     * @param client client for connecting to webservices
     */
    public Grapher(Client client) {
        this.client = client;
    }

    /**
     * Returns all objects in the db.
     * @return all object in the db
     */
    public List<Object> getObjectsList() {
        return client.getObjectsList();
    }

    /**
     * Returns all objects in the db.
     * @return all object in the db
     */
    public Set<Object> getObjectsSet() {
        return client.getObjectsSet();
    }

    /**
     * Returns graph for given root and distance
     * @return graph for given root and distance
     * @throws IdNotFoundException if root id is not valid
     */
    public Graph getGraph() throws IdNotFoundException {
        return client.getGraph(rootId, distance);
    }

    /**
     * Returns root object id.
     * @return root object id
     */
    public int getDistance() {
        return distance;
    }

    /**
     * Sets distance for fetching related objects.
     * @param distance new distance
     */
    public void setDistance(int distance) {
        this.distance = distance;
    }

    /**
     * Returns root object id.
     * @return root object id
     */
    public long getRootId() {
        return rootId;
    }

    /**
     * Sets new root object id
     * @param rootId new root object id
     */
    public void setRootId(long rootId) {
        this.rootId = rootId;
    }
}
