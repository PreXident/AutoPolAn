package cz.cuni.mff.ufal.textan.nametagintegration;

import cz.cuni.mff.ufal.nametag.*;
import cz.cuni.mff.ufal.textan.commons.models.*;
import cz.cuni.mff.ufal.textan.commons.models.Object;

import javax.jws.WebParam;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Vlcak on 29.3.14.
 */
public static class NametagIntegration {
    public static String encodeEntities(String text) {
        return text.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;");
    }


    public static DocumentWithEntities ProcessDocument(Document document) {
        Forms forms = new Forms();
        TokenRanges tokens = new TokenRanges();
        NamedEntities entities = new NamedEntities();
        Scanner reader = new Scanner(document.getText());
        Stack<Integer> openEntities = new Stack<Integer>();
        Ner ner = Ner.load("");
        Tokenizer tokenizer = ner.newTokenizer();
        DocumentWithEntities documentWithEntities = new DocumentWithEntities(document.getText());
        boolean not_eof = true;
        while (not_eof) {
            StringBuilder textBuilder = new StringBuilder();
            String line;

            // Read block
            while ((not_eof = reader.hasNextLine()) && !(line = reader.nextLine()).isEmpty()) {
                textBuilder.append(line);
                textBuilder.append('\n');
            }
            if (not_eof) textBuilder.append('\n');

            // Tokenize and recognize
            String text = textBuilder.toString();
            tokenizer.setText(text);
            int unprinted = 0;
            while (tokenizer.nextSentence(forms, tokens)) {
                ner.recognize(forms, entities);

                for (int i = 0; i < entities.size(); i++) {
                    NamedEntity entity = entities.get(i);
                    int entity_start = (int) tokens.get((int) entity.getStart()).getStart();
                    int entity_end = (int) (tokens.get((int) (entity.getStart() + entity.getLength() - 1)).getStart() + tokens.get((int) (entity.getStart() + entity.getLength() - 1)).getLength());
                    documentWithEntities.AddEntity(new Entity("",entity_start,entity_end,0));

                    /*
                    // Close entities that end sooned than current entity
                    while (!openEntities.empty() && openEntities.peek() < entity_start) {
                        if (unprinted < openEntities.peek()) System.out.print(encodeEntities(text.substring(unprinted, openEntities.peek())));
                        unprinted = openEntities.pop();
                        System.out.print("</ne>");
                    }

                    // Print text just before the entity, open it and add end to the stack
                    if (unprinted < entity_start) System.out.print(encodeEntities(text.substring(unprinted, entity_start)));
                    unprinted = entity_start;
                    System.out.printf("<ne type=\"%s\">", entity.getType());
                    openEntities.push(entity_end);
                    */
                }
/*
                // Close unclosed entities
                while (!openEntities.empty()) {
                    if (unprinted < openEntities.peek()) System.out.print(encodeEntities(text.substring(unprinted, openEntities.peek())));
                    unprinted = openEntities.pop();
                    System.out.print("</ne>");
                }
                */
            }
            // Write rest of the text (should be just spaces)
            if (unprinted < text.length()) System.out.print(encodeEntities(text.substring(unprinted)));
        }
        return documentWithEntities;
    }
}
