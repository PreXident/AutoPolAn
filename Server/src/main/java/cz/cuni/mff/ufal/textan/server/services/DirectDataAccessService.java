package cz.cuni.mff.ufal.textan.server.services;

import cz.cuni.mff.ufal.textan.commons.utils.Pair;
import cz.cuni.mff.ufal.textan.data.repositories.dao.*;
import cz.cuni.mff.ufal.textan.data.tables.*;
import cz.cuni.mff.ufal.textan.server.models.*;
import cz.cuni.mff.ufal.textan.server.models.Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A service which provides direct access into the database.
 *
 * @author Petr Fanta
 */
@Service
@Transactional
public class DirectDataAccessService {

    private final IDocumentTableDAO documentTableDAO;

    private final IObjectTypeTableDAO objectTypeTableDAO;
    private final IObjectTableDAO objectTableDAO;

    private final IRelationTypeTableDAO relationTypeTableDAO;
    private final IRelationTableDAO relationTableDAO;

    private final IInRelationTableDAO inRelationTableDAO;

    /**
     * Instantiates a new Direct data access service.
     * @param documentTableDAO the document table dAO
     * @param objectTypeTableDAO the object type table dAO
     * @param objectTableDAO the object table dAO
     * @param relationTypeTableDAO the relation type table dAO
     * @param relationTableDAO the relation table dAO
     * @param inRelationTableDAO the inRelationTableDAO
     */
    @Autowired
    public DirectDataAccessService(
            IDocumentTableDAO documentTableDAO,
            IObjectTypeTableDAO objectTypeTableDAO,
            IObjectTableDAO objectTableDAO,
            IRelationTypeTableDAO relationTypeTableDAO,
            IRelationTableDAO relationTableDAO, IInRelationTableDAO inRelationTableDAO) {

        this.documentTableDAO = documentTableDAO;
        this.objectTypeTableDAO = objectTypeTableDAO;
        this.objectTableDAO = objectTableDAO;
        this.relationTypeTableDAO = relationTypeTableDAO;
        this.relationTableDAO = relationTableDAO;
        this.inRelationTableDAO = inRelationTableDAO;
    }

    /**
     * Adds a new document document into the system.
     *
     * @param text the text of the new document
     * @return the identifier of the new document
     */
    public long addDocument(String text) {

        DocumentTable documentTable = new DocumentTable(text);

        return documentTableDAO.add(documentTable);
    }

    /**
     * Gets the document with the given id from the system.
     *
     * @param documentId the document id
     * @return the document
     */
    public Document getDocument(long documentId) throws IdNotFoundException{

        DocumentTable documentTable = documentTableDAO.find(documentId);
        if (documentTable == null) {
            throw new IdNotFoundException("documentId", documentId);
        }

        return Document.fromDocumentTable(documentTable);
    }

    /**
     * Gets all documents from the system.
     *
     * @return the documents
     */
    public List<Document> getDocuments() {
        return documentTableDAO.findAll().stream()
                .map(cz.cuni.mff.ufal.textan.server.models.Document::fromDocumentTable)
                .collect(Collectors.toList());
    }

    /**
     * Gets documents which contain object with given id.
     * @param objectId the object id
     * @param firstResult
     *@param maxResults @return a list of documents
     * @throws IdNotFoundException thrown when object with given id not exists
     */
    public Pair<List<Document>, Integer> getDocumentsContainingObject(long objectId, int firstResult, int maxResults) throws IdNotFoundException {

        ObjectTable objectTable = objectTableDAO.find(objectId);
        if (objectTable == null) {
            throw new IdNotFoundException("objectId", objectId);
        }

        int count = documentTableDAO.findAllDocumentsWithObject(objectId).size();
        List<Document> documents = documentTableDAO.findAllDocumentsWithObject(objectId, firstResult, maxResults).stream()
                .map(Document::fromDocumentTable)
                .collect(Collectors.toList());

        return new Pair<>(documents, count);
    }

    /**
     * Update the document with the given.
     *
     * @param documentId the document id
     * @param text the new text of the document
     * @return the indicates if document was updated
     */
    public boolean updateDocument(long documentId, String text) throws IdNotFoundException {

        DocumentTable documentTable = documentTableDAO.find(documentId);
        if (documentTable == null) {
            throw new IdNotFoundException("documentId", documentId);
        }

        documentTable.setText(text);
        documentTableDAO.update(documentTable);

        return true;
    }

    /**
     * Gets all object types.
     *
     * @return the object types
     */
    public List<ObjectType> getObjectTypes() {

        return objectTypeTableDAO.findAll().stream()
                .map(ObjectType::fromObjectTypeTable)
                .collect(Collectors.toList());
    }


    /**
     * Gets the object with given id.
     *
     * @param objectId the object id
     * @return the object
     */
    public Object getObject(long objectId) throws IdNotFoundException {

        ObjectTable objectTable = objectTableDAO.find(objectId);
        if (objectTable == null) {
            throw new IdNotFoundException("objectId", objectId);
        }

        return Object.fromObjectTable(objectTable);
    }

    /**
     * Gets all objects.
     *
     * @return the objects
     */
    public List<Object> getObjects() {

        return objectTableDAO.findAll().stream()
                .map(Object::fromObjectTable)
                .collect(Collectors.toList());

    }

    /**
     * Gets all objects of the given type id.
     *
     * @param objectTypeId the object type id
     * @return the objects
     */
    public List<Object> getObjects(long objectTypeId) {
        //TODO: add test if id exists

        return objectTableDAO.findAllByObjectType(objectTypeId).stream()
                .map(Object::fromObjectTable)
                .collect(Collectors.toList());
    }


    public Pair<List<Object>,Integer> getFilteredObjects(Long objectTypeId, String aliasFilter, int firstResult, int maxResults) throws IdNotFoundException {

        if (objectTypeId != null) {
            ObjectTypeTable objectType = objectTypeTableDAO.find(objectTypeId);
            if (objectType == null) {
                throw new IdNotFoundException("objectTypeId", objectTypeId);
            }
        }

        int count;
        List<ObjectTable> objects;

        if (objectTypeId != null && aliasFilter != null) {

            count = objectTableDAO.findAllByObjectTypeAndAliasSubStr(objectTypeId, aliasFilter).size();
            objects = objectTableDAO.findAllByObjectTypeAndAliasSubStr(objectTypeId, aliasFilter, firstResult, maxResults);

        } else if (objectTypeId != null) {

            count = objectTableDAO.findAllByObjectType(objectTypeId).size();
            objects = objectTableDAO.findAllByObjectType(objectTypeId, firstResult, maxResults);

        } else if (aliasFilter != null) {

            count = objectTableDAO.findAllByAliasSubstring(aliasFilter).size();
            objects = objectTableDAO.findAllByAliasSubstring(aliasFilter, firstResult, maxResults);

        } else {

            count = objectTableDAO.findAll().size();
            objects = objectTableDAO.findAll(firstResult, maxResults);

        }

        return new Pair<>(
                objects.stream()
                    .map(Object::fromObjectTable)
                    .collect(Collectors.toList()),
                count
        );
    }

    /**
     * Finds all objects and their occurrences for a given document.
     * @param documentId the document id
     * @return the pair with the list of object and the list of their occurrences in the document
     * @throws IdNotFoundException thrown when the document identifier not exists
     */
    public Pair<List<Object>, List<Pair<Long, Occurrence>>> getObjectsWithOccurrences(long documentId) throws IdNotFoundException {
        DocumentTable documentTable = documentTableDAO.find(documentId);
        if (documentTable == null) {
            throw new IdNotFoundException("documentId", documentId);
        }

        Set<Object> objects = new HashSet<>();
        List<Pair<Long, Occurrence>> objectOccurrences = new ArrayList<>();

        for (AliasOccurrenceTable aliasOccurrence : documentTable.getAliasOccurrences()) {

            AliasTable alias = aliasOccurrence.getAlias();
            ObjectTable object = alias.getObject();

            objects.add(Object.fromObjectTable(object));
            objectOccurrences.add(new Pair<>(object.getId(), new Occurrence(alias.getAlias(), aliasOccurrence.getPosition())));
        }

        return new Pair<>(new ArrayList<>(objects), objectOccurrences);
    }

    /**
     * Merges two objects into one.
     *
     * @param object1Id the identifier of the first object
     * @param object2Id the identifier of the second object
     * @return the identifier of the new objects
     */
    public long mergeObjects(long object1Id, long object2Id) throws IdNotFoundException {
        //TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Splits merged object.
     *
     * @param objectId the identifier of merged object
     * @return true if object was split, false otherwise
     */
    public boolean splitObject(long objectId) throws IdNotFoundException {
        //TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }


    /**
     * Gets all relation types.
     *
     * @return the relation types
     */
    public List<RelationType> getRelationTypes() {

        return relationTypeTableDAO.findAll().stream()
                .map(RelationType::fromRelationTypeTable)
                .collect(Collectors.toList());

    }

    /**
     * Gets all relations.
     *
     * @return the relations
     */
    public List<Relation> getRelations() {

        return relationTableDAO.findAll().stream()
                .map(Relation::fromRelationTable)
                .collect(Collectors.toList());
    }

    /**
     * Gets all relations of the given type.
     *
     * @param relationTypeId the relation type id
     * @return the relations
     */
    public List<Relation> getRelations(long relationTypeId) {

        //TODO: add test if id exists!

        return relationTableDAO.findAllByRelationType(relationTypeId).stream()
                .map(Relation::fromRelationTable)
                .collect(Collectors.toList());
    }

    /**
     * Finds all relations and their occurrences for a given document.
     * @param documentId the document id
     * @return the pair with the list of relations and the list of their occurrences in the document
     * @throws IdNotFoundException thrown when the document identifier not exists
     */
    public Pair<List<Relation>, List<Pair<Long, Occurrence>>> getRelationsWithOccurrences(long documentId) throws IdNotFoundException{
        DocumentTable documentTable = documentTableDAO.find(documentId);
        if (documentTable == null) {
            throw new IdNotFoundException("documentId", documentId);
        }

        Set<Relation> relations = new HashSet<>();
        List<Pair<Long, Occurrence>> relationOccurrences = new ArrayList<>();

        for (RelationOccurrenceTable relationOccurrence : documentTable.getRelationOccurrences()) {

            RelationTable relation = relationOccurrence.getRelation();

            relations.add(Relation.fromRelationTable(relation));
            relationOccurrences.add(new Pair<>(relation.getId(), new Occurrence(relationOccurrence.getAnchor(), relationOccurrence.getPosition())));
        }

        return new Pair<>(new ArrayList<>(relations), relationOccurrences);
    }

    public List<String> getRolesForRelationType(long relationTypeId) throws IdNotFoundException {

        RelationTypeTable relationType = relationTypeTableDAO.find(relationTypeId);
        if (relationType == null) {
            throw new IdNotFoundException("relationTypeId", relationTypeId);
        }

        return inRelationTableDAO.getRolesForRelationType(relationTypeId);
    }


}
