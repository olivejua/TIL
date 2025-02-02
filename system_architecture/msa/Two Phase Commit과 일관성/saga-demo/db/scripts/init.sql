-- 데이터베이스 초기화
drop database if exists db_order;
create database db_order;

drop database if exists db_product;
create database db_product;

drop database if exists db_payment;
create database db_payment;

-- 유저 생성
create user 'user_order'@'%' identified by 'orderpw';
create user 'user_product'@'%' identified by 'productpw';
create user 'user_payment'@'%' identified by 'paymentpw';

-- 유저 권한 부여
grant all privileges on db_order.* to 'user_order'@'%';
grant all privileges on db_product.* to 'user_product'@'%';
grant all privileges on db_payment.* to 'user_payment'@'%';

-- 주문서비스
use db_order;

create table orders (
    id bigint auto_increment primary key,
    user_id bigint not null, -- 주문자
    amount int not null, -- 주문 금액
    status varchar(20) not null, -- 주문상태
    created_at timestamp not null default current_timestamp, -- 주문일시
    updated_at timestamp not null default current_timestamp,
    index idx_user_id (user_id)
);

create table order_products (
    order_id bigint not null,
    product_id bigint not null, -- 상품 id
    amount int not null, -- 상품 금액
    quantity int not null, -- 구매수량
    primary key (order_id, product_id)
);

-- 상품서비스
use db_product;

create table products (
    id bigint auto_increment primary key,
    name varchar(200) not null,
    amount int not null,
    stock int not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp
);

-- 결제서비스
use db_payment;

create table payment_logs (
    id bigint auto_increment primary key,
    order_id bigint not null,
    amount int not null,
    result_code varchar(50) not null, -- 결제 상태 (200-성공, 401- 유효하지 않은 결제수단, 402-잔액부족, 403-결제사 시스템문제 등등)
    created_at timestamp not null default current_timestamp,
    index idx_order_id (order_id)
);

