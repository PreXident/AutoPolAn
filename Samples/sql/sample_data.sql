USE textan;

-- inserts

INSERT INTO `Document`(`id_document`,`added`,`processed`, `last_change`,`text`)
	VALUES (1, NOW(), NOW(), NOW(), "Josef Novak, známý také pod pseudonymem Pepíček, narozen dne 19.10.1975 byl spatřen dne 18.9.2013 v ulici K novým domům v Praze 5. J. Novak upoutal pozornost postaršího páru, když poškozoval cizí majetek baseballovou pálkou. Policejní hlídka, kterou pár přivolal, po příjezdu na místo činu v 10:28 zajistila podezřelého a důkazy. Podezřelý pan Novák svým chováním poškodil dodávku Ford Tranzit s SPZ AHL 30-24 registrovanou na společnost Čistírna a prádelna Karel Novák a přilehlý stánek s rychlým občerstvením U Mrože. Podezřelý byl následně převezen do vazby a obviněn dle zákona č. 106/1999 Sb. z výtržnictví.");

INSERT INTO `Document`(`id_document`,`added`,`processed`, `last_change`,`text`)
	VALUES (2, NOW(), NOW(), NOW(), "Existují Loukonosy a Juchavice. Ema má maso.");

INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(1, "Osoba");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(2, "Datum");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(3, "Ulice");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(4, "Město");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(5, "Zbraň");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(6, "Čas");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(7, "Typ automobilu");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(8, "SPZ");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(9, "Podnik");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(10, "Zákon");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(11, "Droga");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(12, "Váha");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(13, "Oblečení");
INSERT INTO `ObjectType`(`id_object_type`,`name`) VALUES(14, "ČP");

INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(1, "narozen");
INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(2, "viděn");
INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(3, "poškozoval");
INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(4, "zadržen");
INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(5, "bydlet");
INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(6, "zabit");
INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(7, "vlastnit");
INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(8, "vážit");
INSERT INTO `RelationType`(`id_relation_type`,`name`) VALUES(9, "v");

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (1, 1, 1, "");
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (1, 1, "Josef Novak");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(1, 1, 1, 0);
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (2, 1, "Pepíček");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(2, 2, 1, 40);
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (3, 1, "J. Novak");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(3, 3, 1, 131);
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (4, 1, "Novák");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(4, 4, 1, 344);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (2, 2, 2, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (5, 2, "19.10.1975");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(5, 5, 1, 61);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (3, 3, 2, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (6, 3, "18.9.2013");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(6, 6, 1, 88);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (4, 4, 3, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (7, 4, "K novým domům");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(7, 7, 1, 106);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (5, 5, 4, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (8, 5, "Praze 5");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(8, 8, 1, 122);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (6, 6, 5, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (9, 6, "baseballovou pálkou");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(9, 9, 1, 204);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (7, 7, 4, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (10, 7, "Loukonosy");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(10, 10, 2, 9);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (8, 8, 4, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (11, 8, "Juchavice");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(11, 11, 2, 21);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (9, 9, 1, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (12, 9, "Ema");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(12, 12, 2, 32);

INSERT INTO `Object`(`id_object`, `id_root_object`,`id_object_type`,`data`) VALUES (10, 10, 11, ""); 
	INSERT INTO `Alias`(`id_alias`,`id_object`,`alias`) VALUES (13, 10, "maso");
		INSERT INTO `AliasOccurrence`(`id_alias_occurrence`,`id_alias`,`id_document`,`position`) VALUES(13, 13, 2, 39);

INSERT INTO `Relation`(`id_relation`,`id_relation_type`) VALUES (1, 1);
	INSERT INTO `RelationOccurrence`(`id_relation_occurrence`,`id_relation`,`id_document`,`position`,`anchor`) VALUES (1, 1, 1, 49, "narozen");

	INSERT INTO `IsInRelation`(`id_is_in_relation`, `id_relation`, `id_object`,`order_in_relation`) VALUES (1, 1, 1, 0);
	INSERT INTO `IsInRelation`(`id_is_in_relation`, `id_relation`, `id_object`,`order_in_relation`) VALUES (2, 1, 2, 0);

INSERT INTO `Relation`(`id_relation`,`id_relation_type`) VALUES (2, 7);
	INSERT INTO `RelationOccurrence`(`id_relation_occurrence`,`id_relation`,`id_document`,`position`,`anchor`) VALUES (2, 2, 2, 36, "má");

	INSERT INTO `IsInRelation`(`id_is_in_relation`, `id_relation`, `id_object`,`order_in_relation`, `role`) VALUES (3, 2, 9, -1, "kdo");
	INSERT INTO `IsInRelation`(`id_is_in_relation`, `id_relation`, `id_object`,`order_in_relation`, `role`) VALUES (4, 2, 10, 0, "co");