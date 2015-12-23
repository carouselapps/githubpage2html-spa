-- name:save-page!
-- creates a new page
INSERT INTO page
(url, content, timestamp)
VALUES (:url, :content, :timestamp)

-- name:update-page!
-- updates an existing page
UPDATE page
SET content = :content, timestamp = :timestamp
WHERE url = :url

-- name:get-pages
-- selects all available pages
SELECT * FROM page ORDER BY timestamp DESC

-- name:-get-page
-- selects page with url :url
SELECT * FROM page WHERE url = :url

-- name:-delete-page!
-- deletes page with id :id
DELETE FROM page WHERE id = :id