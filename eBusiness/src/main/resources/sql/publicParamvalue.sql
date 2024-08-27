create table if not exists t_public_paramvalue (
    paramname varchar(100) NOT NULL,
    paramvalue varchar(2000),
    module varchar(100),
    memo varchar(500)
);

-- 系统环境变量配置
MERGE INTO t_public_paramvalue AS target
    USING (
        SELECT 'config' AS paramname, '{"type":0}' AS paramvalue, 'system' AS module, '' AS memo
    ) AS source
    ON (target.paramname = source.paramname AND target.module = source.module)
    WHEN MATCHED THEN
        UPDATE SET paramvalue = target.paramvalue
    WHEN NOT MATCHED THEN
        INSERT (paramname, paramvalue, module, memo)
            VALUES (source.paramname, source.paramvalue, source.module, source.memo);

