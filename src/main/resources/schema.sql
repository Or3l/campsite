DROP TABLE IF EXISTS  date_availability;
DROP TABLE IF EXISTS  booking;
create table if not exists booking
(
    id uuid not null
        constraint booking_pk
        primary key,
    email text not null,
    full_name text not null,
    arrival date not null,
    departure date not null,
    created_at timestamp default now(),
    updated_at timestamp
);


create table if not exists date_availability
(
    day date not null
        constraint date_pk
        primary key,
    is_booked boolean default false,
    version integer default 0,
    booking_id uuid
        constraint date_availability_booking_id_fk
        references booking
);