package cz.cuni.mff.ufal.textan.server.models;

import cz.cuni.mff.ufal.textan.commons.utils.Pair;
import cz.cuni.mff.ufal.textan.data.tables.RelationOccurrenceTable;
import cz.cuni.mff.ufal.textan.data.tables.RelationTable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A service layer representation of the Relation
 * @author Petr Fanta
 */
public class Relation {

    private final long id;
    private final RelationType type;
    private final List<Pair<Long, Pair<String,Integer>>> objectsInRelation;
    private final List<String> anchors;
    private final boolean isNew;


    /**
     * Instantiates a new Relation.
     * @param id the id
     * @param type the type
     * @param objectsInRelation the objects in relation
     * @param anchors
     * @param isNew the is new
     */
    public Relation(long id, RelationType type, List<Pair<Long, Pair<String,Integer>>> objectsInRelation, List<String> anchors, boolean isNew) {
        this.id = id;
        this.type = type;
        this.objectsInRelation = objectsInRelation;
        this.anchors = anchors;
        this.isNew = isNew;
    }

    /**
     * Creates a {@link cz.cuni.mff.ufal.textan.server.models.Relation} from a {@link cz.cuni.mff.ufal.textan.data.tables.RelationTable}.
     *
     * @param relationTable the relation table
     * @return the relation
     */
    public static Relation fromRelationTable(RelationTable relationTable) {

        List<Pair<Long, Pair<String,Integer>>> objectsInRelation = relationTable.getObjectsInRelation().stream()
                .map(inRelation -> new Pair<>(inRelation.getObject().getId(), new Pair<>(inRelation.getRole(), inRelation.getOrder())))
                .collect(Collectors.toList());

//        List<String> anchors = new ArrayList<>(relationTable.getOccurrences().stream()
//                .map(RelationOccurrenceTable::getAnchor)
//                .collect(Collectors.toSet()));

        //TODO: test if this is unique (distinct)
        List<String> anchors = relationTable.getOccurrences().stream()
                .map(RelationOccurrenceTable::getAnchor)
                .collect(Collectors.toList());

        return new Relation(
                relationTable.getId(),
                RelationType.fromRelationTypeTable(relationTable.getRelationType()),
                objectsInRelation,
                anchors,
                false
                );
    }

    public static Relation fromCommonsRelation(cz.cuni.mff.ufal.textan.commons.models.Relation commonsRelation) {
        return new Relation(
                commonsRelation.getId(),
                RelationType.fromCommonsRelationType(commonsRelation.getRelationType()),
                commonsRelation.getObjectInRelationIds().getInRelations().stream().map(x -> new Pair<>(x.getObjectId(), new Pair<>(x.getRole(), x.getOrder()))).collect(Collectors.toList()),
                commonsRelation.getAnchors(),
                commonsRelation.isIsNew()
        );
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public RelationType getType() {
        return type;
    }

    /**
     * Gets identifiers of objects in relation and they order in relation.
     *
     * @return the objects in relation
     */
    public List<Pair<Long, Pair<String,Integer>>> getObjectsInRelation() {
        return objectsInRelation;
    }


    /**
     * Gets anchors.
     *
     * @return the anchors
     */
    public List<String> getAnchors() {
        return anchors;
    }

    /**
     * Indicates if relation is in database.
     *
     * @return the boolean
     */
    public boolean isNew() {
        return isNew;
    }

    /**
     * Converts the instance to a {@link cz.cuni.mff.ufal.textan.commons.models.Relation}.
     *
     * @return the {@link cz.cuni.mff.ufal.textan.commons.models.Relation}
     */
    public cz.cuni.mff.ufal.textan.commons.models.Relation toCommonsRelation() {

        cz.cuni.mff.ufal.textan.commons.models.Relation commonsRelation = new cz.cuni.mff.ufal.textan.commons.models.Relation();
        commonsRelation.setId(id);
        commonsRelation.setRelationType(type.toCommonsRelationType());

        cz.cuni.mff.ufal.textan.commons.models.Relation.ObjectInRelationIds objectInRelationIds = new cz.cuni.mff.ufal.textan.commons.models.Relation.ObjectInRelationIds();
        for (Pair<Long, Pair<String,Integer>> inRelation : objectsInRelation) {
            cz.cuni.mff.ufal.textan.commons.models.Relation.ObjectInRelationIds.InRelation commonsInRelation = new cz.cuni.mff.ufal.textan.commons.models.Relation.ObjectInRelationIds.InRelation();
            commonsInRelation.setObjectId(inRelation.getFirst());
            commonsInRelation.setRole(inRelation.getSecond().getFirst());
            commonsInRelation.setOrder(inRelation.getSecond().getSecond());

            objectInRelationIds.getInRelations().add(commonsInRelation);
        }
        commonsRelation.setObjectInRelationIds(objectInRelationIds);
        commonsRelation.getAnchors().addAll(anchors);

        commonsRelation.setIsNew(isNew);

        return commonsRelation;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Relation relation = (Relation) o;

        if (id != relation.id) return false;
        if (type != null ? !type.equals(relation.type) : relation.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Relation{");
        sb.append("id=").append(id);
        sb.append(", type=").append(type);
        sb.append(", objectsInRelation=").append(objectsInRelation);
        sb.append(", anchors=").append(anchors);
        sb.append(", isNew=").append(isNew);
        sb.append('}');
        return sb.toString();
    }
}
