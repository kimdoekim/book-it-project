-- 뱃지 초기 데이터 SQL 스크립트 (src/main/resources/data.sql)

-- 읽은 책 수 뱃지
INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('독서 입문', '첫 번째 책을 완독했습니다!', '/images/badges/book-beginner.png', 'BOOKS_READ', 1);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('책벌레 초보', '5권의 책을 읽었습니다!', '/images/badges/book-novice.png', 'BOOKS_READ', 5);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('꾸준한 독자', '10권의 책을 읽었습니다!', '/images/badges/book-reader.png', 'BOOKS_READ', 10);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('열정적인 독자', '25권의 책을 읽었습니다!', '/images/badges/book-enthusiast.png', 'BOOKS_READ', 25);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('책 마스터', '50권의 책을 읽었습니다!', '/images/badges/book-master.png', 'BOOKS_READ', 50);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('지식의 대가', '100권의 책을 읽었습니다!', '/images/badges/book-guru.png', 'BOOKS_READ', 100);

-- 읽은 페이지 수 뱃지
INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('첫 걸음', '500 페이지를 읽었습니다!', '/images/badges/pages-beginner.png', 'PAGES_READ', 500);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('페이지 넘기기', '1,000 페이지를 읽었습니다!', '/images/badges/pages-novice.png', 'PAGES_READ', 1000);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('페이지 여행자', '5,000 페이지를 읽었습니다!', '/images/badges/pages-explorer.png', 'PAGES_READ', 5000);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('페이지 탐험가', '10,000 페이지를 읽었습니다!', '/images/badges/pages-voyager.png', 'PAGES_READ', 10000);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('페이지 마스터', '25,000 페이지를 읽었습니다!', '/images/badges/pages-master.png', 'PAGES_READ', 25000);

-- 완료한 목표 수 뱃지
INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('첫 목표 달성', '첫 번째 독서 목표를 달성했습니다!', '/images/badges/goal-first.png', 'GOALS_COMPLETED', 1);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('목표 추진자', '5개의 독서 목표를 달성했습니다!', '/images/badges/goal-achiever.png', 'GOALS_COMPLETED', 5);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('목표 마스터', '10개의 독서 목표를 달성했습니다!', '/images/badges/goal-master.png', 'GOALS_COMPLETED', 10);

-- 연속 독서일 뱃지
INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('3일 연속', '3일 연속으로 독서했습니다!', '/images/badges/streak-3days.png', 'STREAK', 3);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('일주일 연속', '7일 연속으로 독서했습니다!', '/images/badges/streak-week.png', 'STREAK', 7);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('2주 연속', '14일 연속으로 독서했습니다!', '/images/badges/streak-2weeks.png', 'STREAK', 14);

INSERT INTO badges (name, description, image_url, type, threshold)
VALUES ('한 달 연속', '30일 연속으로 독서했습니다!', '/images/badges/streak-month.png', 'STREAK', 30);