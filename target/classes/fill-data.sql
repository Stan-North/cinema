DO $$
	DECLARE
	counter INT;
	first_row_prefix VARCHAR := 'A';
	second_row_prefix VARCHAR := 'B';
	BEGIN
		FOR i IN 1..5 LOOP
		INSERT INTO seat (title)
		VALUES(first_row_prefix || i);
		END LOOP;

		FOR i IN 1..5 LOOP
		INSERT INTO seat (title)
		VALUES(second_row_prefix || i);
		END LOOP;
	END;
$$;
