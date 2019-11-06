-- this was length 8 and 70 :) we can (and should) go bigger here, these are email-adresses
CREATE TABLE addressbook(userid VARCHAR(255), entry VARCHAR(255) NOT NULL);
CREATE TABLE preferences(userid VARCHAR(255) NOT NULL, key VARCHAR(128) NOT NULL, entry VARCHAR(1024) NOT NULL);
CREATE INDEX addressbook_idx ON addressbook(userid);