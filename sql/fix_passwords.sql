-- Fix password hashes
-- System uses MD5(username + password), not MD5(password)
-- Update passwords to use correct encryption

UPDATE sys_user 
SET password_hash = LOWER(MD5(CONCAT(username, 'admin123')))
WHERE username = 'admin';

UPDATE sys_user 
SET password_hash = LOWER(MD5(CONCAT(username, '123456')))
WHERE username = 'farmer1';

UPDATE sys_user 
SET password_hash = LOWER(MD5(CONCAT(username, '123456')))
WHERE username = 'buyer1';

-- Verify the updates
SELECT username, password_hash, 
       LOWER(MD5(CONCAT(username, CASE 
         WHEN username = 'admin' THEN 'admin123'
         ELSE '123456'
       END))) AS calculated_hash
FROM sys_user
WHERE username IN ('admin', 'farmer1', 'buyer1');
