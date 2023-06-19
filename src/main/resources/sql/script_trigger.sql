CREATE OR REPLACE FUNCTION insert_checker(kd numeric, platform integer, opus integer)
RETURNS VOID
LANGUAGE plpgsql
AS $$
    DECLARE
        v_ integer;
    BEGIN
        SELECT amount INTO v_ FROM stats WHERE kda = kd AND id_platform = platform;
        IF v_ IS NULL THEN
            INSERT INTO stats (kda, amount, id_opus, id_platform) VALUES (kd, 1, opus, platform);
        ELSE
            UPDATE stats SET amount = v_ + 1 WHERE kda = kd AND id_platform = platform;
        END IF;
    END;
$$;