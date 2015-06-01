DROP TABLE todos if EXISTS;

CREATE sequence todos_seq;

CREATE TABLE todos (
    id IDENTITY not null,
    title VARCHAR(300),
    createdTS TIMESTAMP,
    completedTS TIMESTAMP,
    done BOOLEAN 
);
