SELECT count(*) as count, ai_queries.query, avg(exec_time) as exec_time FROM ai_queries
LEFT JOIN ai_intents ON (ai_intents.id = ai_queries.ai_intent_id)
GROUP BY ai_queries.query, ai_queries.ai_intent_id
ORDER BY count DESC