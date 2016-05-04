# activerecord2java

Library built to easily access data (based GORM) and have code clean of domain class. 

Pattern active record core library in java with hibernate

Embeds entity manager in it and provides a bunch of DAL abstractions to make data access a lot simpler. This allows activerecord pattern style of usage,

 * Person.get(Person.class, 1L);
 * Person.findBy(Person.class, "firstName", "Fabricio", "lastName", "Conde");
 * Person.findAllBy(Person.class, "firstName", "Fabricio");
 * Person.findAll(Person.class);
 * Person.count(Person.class);
 * Person.exists(Person.class, 1L);
 * personInstance.save();
 * personInstance.delete();
 * personInstance.update();
 * personInstance.flush();
 * personInstance.refresh();

Exists 2 diferents codes (version) of library, you should choose which is better to you.

A first version, I wrote to use with hibernate without JPA (only hibernate) with support hibernate 3. A second, I wrote to support the new version hibernate (5).

Both versions run hibernate without JPA.
