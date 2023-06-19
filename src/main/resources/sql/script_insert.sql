INSERT INTO public.platform (id_platform, label)
VALUES
    (1, 'Playstation'),
    (2, 'Xbox'),
    (3, 'Battle.net'),
    (4, 'Steam')
ON CONFLICT(id_platform) DO UPDATE SET id_platform = EXCLUDED.id_platform, label = EXCLUDED.label;

INSERT INTO opus (name)
VALUES
    ('bo3'),
    ('iw'),
    ('wwii'),
    ('bo4'),
    ('mw'),
    ('cw'),
    ('vg')
ON CONFLICT DO NOTHING;
