CREATE TABLE resource(
   resource_id serial PRIMARY KEY,
   uuid VARCHAR (50) UNIQUE NOT NULL,
   path VARCHAR NOT NULL,
   relative_path VARCHAR NOT NULL,
   created_on TIMESTAMP NOT NULL,
   update_on TIMESTAMP
);
