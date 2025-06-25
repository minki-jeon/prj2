USE prj2;
# Tables = 'board', 'member'

CREATE TABLE member
(
    id         VARCHAR(100)   NOT NULL PRIMARY KEY,
    password   VARCHAR(255)   NOT NULL,
    nickname   VARCHAR(100)   NOT NULL UNIQUE,
    info       VARCHAR(10000) NULL,
    created_at DATETIME       NOT NULL DEFAULT NOW()
);

CREATE TABLE board
(
    seq        INT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    title      VARCHAR(500)       NOT NULL,
    content    VARCHAR(10000)     NOT NULL,
    writer     VARCHAR(100)       NOT NULL,
    id         VARCHAR(100)       NOT NULL,
    created_at DATETIME           NOT NULL DEFAULT NOW(),
    FOREIGN KEY (id) REFERENCES member (id)
);
## TODO 1 : update_at, delete_at, delete_yn, secret_yn
## TODO 2 : REPPLE

