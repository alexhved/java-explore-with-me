create table if not exists stats (
    id bigint generated by default as identity primary key ,
    app varchar(20),
    uri varchar(50),
    ip varchar(20),
    timestamp timestamp
);