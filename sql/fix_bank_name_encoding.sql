-- 修复银行名称编码问题
-- 使用UNHEX函数直接更新UTF-8编码的十六进制值

UPDATE bank_info SET bank_name = UNHEX('E4B8ADE59BBDE5869CE4B89AE993B6E8A18C') WHERE id = 1; -- '中国农业银行'
UPDATE bank_info SET bank_name = UNHEX('E4B8ADE59BBDE993B6E8A18C') WHERE id = 2; -- '中国银行'

-- 验证修复结果
SELECT id, bank_name FROM bank_info;

