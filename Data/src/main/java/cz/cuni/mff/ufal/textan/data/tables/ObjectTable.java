package cz.cuni.mff.ufal.textan.data.tables;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;

import javax.persistence.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Object (entity) itself
 *
 * @author Vaclav Pernicka
 */
@Entity
@Indexed(index = "ObjectIndex")
@Table(name = "Object")
public class ObjectTable extends AbstractTable {

    public static final String PROPERTY_NAME_ID = "id";
    public static final String PROPERTY_NAME_OBJECT_TYPE_ID = "objectType";
    public static final String PROPERTY_NAME_ALIASES_ID = "aliases";
    public static final String PROPERTY_NAME_GLOBAL_VERSION = "globalVersion";
    public static final String PROPERTY_NAME_ROOT_OBJECT_ID = "rootObject";

    private long id;
    private String data;
    private long globalVersion;

    private ObjectTable rootObject;
    private ObjectTypeTable objectType;
    private Set<AliasTable> aliases = new HashSet<>();
    private Set<InRelationTable> relations = new HashSet<>();

    private Set<ObjectTable> rootOfObjects = new HashSet<>();
    private JoinedObjectsTable newObject;
    private JoinedObjectsTable oldObject1;
    private JoinedObjectsTable oldObject2;

    public ObjectTable() {
        rootObject = this;
        rootOfObjects.add(this);
    }

    public ObjectTable(ObjectTypeTable objectType) {
        this();
        this.objectType = objectType;
        objectType.getObjectsOfThisType().add(this);
    }

    public ObjectTable(String data, ObjectTypeTable objectType) {
        this();
        this.data = data;
        this.objectType = objectType;
        objectType.getObjectsOfThisType().add(this);
    }

    @Id
    @GeneratedValue
    @DocumentId
    @Column(name = "id_object", nullable = false, unique = true)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "data", nullable = true)
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @Cascade(CascadeType.SAVE_UPDATE)
    @JoinColumn(name = "id_root_object")
    @ContainedIn
    @IndexedEmbedded(includePaths = "id")
    public ObjectTable getRootObject() {
        return rootObject;
    }

    public void setRootObject(ObjectTable rootObject) {
        this.rootObject = rootObject;
    }

    @Transient
    public boolean isRoot() {
        return this == rootObject;
    }

    @OneToMany(mappedBy = "rootObject")
    @Cascade(CascadeType.SAVE_UPDATE)
    @IndexedEmbedded(includePaths = "aliases.alias")
    public Set<ObjectTable> getRootOfObjects() {
        return rootOfObjects;
    }

    public void setRootOfObjects(Set<ObjectTable> rootOfObjects) {
        this.rootOfObjects = rootOfObjects;
    }

    @Column(name = "globalversion", nullable = false)
    public long getGlobalVersion() {
        return globalVersion;
    }

    public void setGlobalVersion(long globalVersion) {
        this.globalVersion = globalVersion;
    }

    @ManyToOne
    @JoinColumn(name = "id_object_type", nullable = false)
    @IndexedEmbedded(includePaths = "id")
    public ObjectTypeTable getObjectType() {
        return objectType;
    }

    public void setObjectType(ObjectTypeTable objectType) {
        this.objectType = objectType;
    }

    @OneToMany(mappedBy = "object")
    @IndexedEmbedded(includePaths = "alias")
    @ContainedIn
    public Set<AliasTable> getAliases() {
        return aliases;
    }

    public void setAliases(Set<AliasTable> aliases) {
        this.aliases = aliases;
    }

    @OneToMany(mappedBy = "object")
    public Set<InRelationTable> getRelations() {
        return relations;
    }

    public void setRelations(Set<InRelationTable> relations) {
        this.relations = relations;
    }

    @OneToOne(mappedBy = "newObject")
    @Cascade(CascadeType.DELETE)
    public JoinedObjectsTable getNewObject() {
        return newObject;
    }

    public void setNewObject(JoinedObjectsTable newObject) {
        this.newObject = newObject;
    }

    @OneToOne(mappedBy = "oldObject1")
    @Cascade(CascadeType.DELETE)
    public JoinedObjectsTable getOldObject1() {
        return oldObject1;
    }

    public void setOldObject1(JoinedObjectsTable oldObject1) {
        this.oldObject1 = oldObject1;
    }

    @OneToOne(mappedBy = "oldObject2")
    @Cascade(CascadeType.DELETE)
    public JoinedObjectsTable getOldObject2() {
        return oldObject2;
    }

    public void setOldObject2(JoinedObjectsTable oldObject2) {
        this.oldObject2 = oldObject2;
    }

    /**
     * <b>Changes in returned set do not propagate into database!</b>
     *
     * @return All objects this object WAS composed from
     */
    @Transient
    public Set<ObjectTable> getObjectsThisWasJoinedFrom() {
        Set<ObjectTable> result = new HashSet<>();
        if (getNewObject() == null) return result;

        result.add(getNewObject().getOldObject1());
        result.add(getNewObject().getOldObject2());

        result.addAll(getNewObject().getOldObject1().getObjectsThisWasJoinedFrom());
        result.addAll(getNewObject().getOldObject2().getObjectsThisWasJoinedFrom());
        return result;
    }

    /**
     * <b>Changes in returned set do not propagate into database!</b>
     *
     * @return All objects this object IS composed from
     */
    @Transient
    public Set<ObjectTable> getObjectsThisIsJoinedFrom() {
        Set<ObjectTable> result = new HashSet<>();
        if (getNewObject() == null) return result;

        if (getNewObject().getTo() == null || getNewObject().getTo().after(Calendar.getInstance().getTime())) {
            result.add(getNewObject().getOldObject1());
            result.add(getNewObject().getOldObject2());
            result.addAll(getNewObject().getOldObject1().getObjectsThisWasJoinedFrom());
            result.addAll(getNewObject().getOldObject2().getObjectsThisWasJoinedFrom());
        }
        return result;
    }

    @Override
    public String toString() {
        return "ObjectT{" + "id=" + id + ", root=" + rootObject.getId() + ", globalVersion=" + globalVersion + ", data=" + data + ", objectType=" + objectType + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof ObjectTable)) return false;
        ObjectTable ot = (ObjectTable) o;
        return ot.getId() == this.getId() && ot.getData().equals(this.getData()) && ot.getObjectType().equals(this.getObjectType());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        hash = 79 * hash + Objects.hashCode(this.data);
        hash = 79 * hash + Objects.hashCode(this.objectType);
        return hash;
    }
}
