-- Ejecutar en Supabase > SQL Editor DESPUÉS de levantar los 3 microservicios por primera vez
-- (Hibernate crea las tablas automáticamente con ddl-auto=update).
-- Activa Row Level Security para que la API pública de Supabase (anon key) no pueda leer ni escribir.
-- Los microservicios se conectan con el rol "postgres" por JDBC, que no se ve afectado por RLS.
ALTER TABLE IF EXISTS public.productos    ENABLE ROW LEVEL SECURITY;
ALTER TABLE IF EXISTS public.reservas     ENABLE ROW LEVEL SECURITY;
ALTER TABLE IF EXISTS public.comprobantes ENABLE ROW LEVEL SECURITY;
