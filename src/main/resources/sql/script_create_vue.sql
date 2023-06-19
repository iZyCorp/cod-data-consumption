CREATE OR REPLACE VIEW stats_view AS
SELECT s.kda AS "Ratio K/D",
       s.amount AS "Nombre de joueur",
       o.name AS "Jeu",
       p.label AS "Plateforme",
       sum(s.amount) OVER () AS "Nombre de joueur recensé",
       sum(s.amount) OVER () / 20 AS "Nombre de page recensé"
FROM Stats s
         INNER JOIN Platform p ON s.id_platform = p.id_platform
         INNER JOIN Opus o ON s.id_opus = o.id_opus
ORDER BY s.kda ASC;