
set search_path to public,field_manage;

insert into field_user (last_name, first_name, app_admin_b, created_datetz)
values ('Farley', 'Jim', true, current_timestamp);

update field_user set app_login = 'farleyjim', 
app_encpassword = pgp_sym_encrypt('password', 'aes')
where field_user_id = 1;

select pgp_sym_decrypt(app_encpassword::bytea, 'aes') from field_user
where pgp_sym_decrypt(app_encpassword::bytea, 'aes') ='password';

•	INSERT INTO field(field_name, field_geom, created_datetz) 
VALUES ('Dad''s Cultivated', 
ST_Transform(ST_GeomFromText('POLYGON ((-98.65070837415604 32.11573800144451, 
-98.65068476156632 32.11272911539712, 
-98.64202112847605 32.11256941997059,
-98.64220500984801 32.11965087237952, 
-98.65042344396167 32.1195293065847,
-98.65035994776281 32.11814307220845,
-98.64557713898905 32.11811726268408,
-98.64545238256078 32.11382619598167,
-98.647344869717 32.11378472039517,
-98.6475887771383 32.11564451081091, 
-98.65070837415604 32.11573800144451))',4326),4326), current_timestamp) 
RETURNING field_id;

•	INSERT INTO field(field_name, field_geom, created_datetz) 
VALUES ('Mom''s Tifton', 
ST_Transform (ST_GeomFromText('POLYGON ((-98.65615282876533 32.11240724346765,
-98.65614029405009 32.10938020882313,
-98.65083573198936 32.10937395329969,
-98.65082758527537 32.11245023125252,
-98.65615282876533 32.11240724346765))',4326),4326), current_timestamp) 
RETURNING field_id;


•	INSERT INTO field(field_name, field_geom, created_datetz)  VALUES ('Spruill Pivot', ST_Transform(ST_Force2D(ST_GeomFromKML('<Polygon><tessellate>1</tessellate>
<outerBoundaryIs><LinearRing>
<coordinates>-98.61937891559366,32.11745075313696,0 -98.62048693823859,32.11437816465637,0 -98.61432749628111,32.11271745766126,0 -98.61317965949985,32.11567421223258,0 -98.61685594392591,32.11669632782378,0 -98.61907073078436,32.11762595989298,0 -98.61937891559366,32.11745075313696,0 </coordinates>
</LinearRing></outerBoundaryIs></Polygon>')),4326), current_timestamp) RETURNING field_id;

•	  INSERT INTO field(field_name, field_geom, created_datetz) VALUES ('Underwood', ST_Transform(ST_Force2D(ST_GeomFromGeoJSON('{ "type": "Polygon", "coordinates": [ [ [ -98.753613128578579, 32.159439629851128, 0.0 ], [ -98.753645688705845, 32.158532385898653, 0.0 ], [ -98.753668129741726, 32.157815848923001, 0.0 ], [ -98.753754122779398, 32.156657960157183, 0.0 ], [ -98.753747807369024, 32.155989927040068, 0.0 ], [ -98.753717397281406, 32.154209677788188, 0.0 ], [ -98.753656382136256, 32.153172101503053, 0.0 ], [ -98.753642625993194, 32.152721654373273, 0.0 ], [ -98.752508318791044, 32.152679883297203, 0.0 ], [ -98.751751584507488, 32.152849607902333, 0.0 ], [ -98.751431167737209, 32.152491993781531, 0.0 ], [ -98.751290840246995, 32.152001796849497, 0.0 ], [ -98.751341895875584, 32.150939242803311, 0.0 ], [ -98.751418532605385, 32.150180353063753, 0.0 ], [ -98.751403085948141, 32.149496163529292, 0.0 ], [ -98.751408322468009, 32.148894214229237, 0.0 ], [ -98.750007394426873, 32.148894851261439, 0.0 ], [ -98.748835838770319, 32.148863649547422, 0.0 ], [ -98.747904758638896, 32.148911694807879, 0.0 ], [ -98.747054098728313, 32.148848615427838, 0.0 ], [ -98.746954659813582, 32.149365531779218, 0.0 ], [ -98.746947670736162, 32.150007805323597, 0.0 ], [ -98.746131264769701, 32.150574645829657, 0.0 ], [ -98.745384048310726, 32.151109969989129, 0.0 ], [ -98.745354015317304, 32.15230843003399, 0.0 ], [ -98.745401787173449, 32.155003064800127, 0.0 ], [ -98.745390242217539, 32.156742244456197, 0.0 ], [ -98.745440587071542, 32.15787642007119, 0.0 ], [ -98.746603395438953, 32.157905474002099, 0.0 ], [ -98.746562884565847, 32.159249504547311, 0.0 ], [ -98.749108164177244, 32.159408427931957, 0.0 ], [ -98.75097419434185, 32.159440120962863, 0.0 ], [ -98.752766637091696, 32.159439781933649, 0.0 ], [ -98.753613128578579, 32.159439629851128, 0.0 ] ] ] } ')),4326), current_timestamp) RETURNING field_id;




insert into field_grower (field_id, grower_id, created_datetz)
values
(2, 1, current_timestamp);
insert into field_grower (field_id, grower_id, created_datetz)
values
(3, 1, current_timestamp);
insert into field_grower (field_id, grower_id, created_datetz)
values
(4, 1, current_timestamp);
insert into field_grower (field_id, grower_id, created_datetz)
values
(5, 1, current_timestamp);
insert into field_grower (field_id, grower_id, created_datetz)
values
(6, 1, current_timestamp);

insert into field_owner (field_id, owner_id, created_datetz)
values
(2, 1, current_timestamp);
insert into field_owner (field_id, owner_id, created_datetz)
values
(3, 1, current_timestamp);
insert into field_owner (field_id, owner_id, created_datetz)
values
(4, 1, current_timestamp);
insert into field_owner (field_id, owner_id, created_datetz)
values
(5, 1, current_timestamp);
insert into field_owner (field_id, owner_id, created_datetz)
values
(6, 1, current_timestamp);

********************S

select field.field_name, 
ST_AsGeoJSON(ST_Transform(field.field_geom, 3857)) as field_boundary, 
field_user.first_name|| ' ' ||field_user.last_name as grower
from field 
left outer join field_grower 
on field.field_id = field_grower.field_id 
inner join field_user
on field_grower.grower_id = field_user.field_user_id;

****************

select field.field_name, 
ST_AsGeoJSON(ST_Transform(field.field_geom, 3857)) as field_boundary, 
field_user.first_name|| ' ' ||field_user.last_name as grower
from field 
left outer join field_grower 
on field.field_id = field_grower.field_id 
inner join field_user
on field_grower.grower_id = field_user.field_user_id
union

select field.field_name, 
ST_AsGeoJSON(field.field_geom) as field_boundary, 
field_user.first_name|| ' ' ||field_user.last_name as grower
from field 
left outer join field_grower 
on field.field_id = field_grower.field_id 
inner join field_user
on field_grower.grower_id = field_user.field_user_id

order by field_name;


select (st_area(st_transform(field_geom,26914)) * 0.00024710538146717) as area_acres from field where field_id = 6;


•	UPDATE field SET field_attributes = '{ "crop": "cotton", "irrigation": "none"}' WHERE field_id=2;

UPDATE field SET field_attributes = 
		'{ "crop": "hay", "irrigation": "none"}'
	WHERE field_id=3;


	SELECT field_name, field_attributes ->> 'irrigation' as irrigation_system, field_attributes ->> 'crop' as crop, 
ST_Contains(ST_Transform(ST_MakeEnvelope(-98.7540438321756397,32.1050599181344865,-98.5599342262939757,32.2062253936398903,4326), 4326),field_geom)
FROM field 
WHERE
field_attributes ->> 'crop' = 'cotton';


SELECT fieldA.field_name, fieldB.field_name, 
ST_Distance(ST_Transform(fieldA.field_geom,26914), ST_Transform(fieldB.field_geom,26914)) * 0.000621371 as distance_miles
FROM field as fieldA CROSS JOIN field as fieldB
WHERE fieldA.field_name != fieldB.field_name;

select field_name, ST_Perimeter(ST_Transform(field_geom,26914)) * 0.000621371 as perimeter_miles
FROM field;

•	SELECT fieldA.field_name, fieldB.field_name, ST_Distance(ST_Transform(fieldA.field_geom,26914),ST_Transform(fieldB.field_geom,26914)) * 0.000621371 as distance_miles 
FROM field as fieldA CROSS JOIN field as fieldB WHERE fieldA.field_name != fieldB.field_name 
and ST_DWithin(ST_Transform(fieldA.field_geom,26914), ST_Transform(fieldB.field_geom,26914),1609.34*2::double precision);
