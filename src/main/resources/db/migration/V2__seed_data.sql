--
-- PostgreSQL database dump
--

-- Dumped from database version 16.9
-- Dumped by pg_dump version 16.9

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: admin
--

INSERT INTO public.roles VALUES (1, 'ADMIN');
INSERT INTO public.roles VALUES (2, 'MANAGER');
INSERT INTO public.roles VALUES (3, 'USER');
INSERT INTO public.roles VALUES (4, 'STAFF');


--
-- Data for Name: accounts; Type: TABLE DATA; Schema: public; Owner: admin
--

INSERT INTO public.accounts VALUES (1, 'admin@gmail.com', 'Admin', '$2a$10$PGWSCwHGRv8wlxNwfQ1DE.LH8odMOx52q/GmT.Ue2/xAMfU0VtJXG', 1);
INSERT INTO public.accounts VALUES (2, 'manager@gmail.com', 'Manager', '$2a$10$PGWSCwHGRv8wlxNwfQ1DE.LH8odMOx52q/GmT.Ue2/xAMfU0VtJXG', 2);
INSERT INTO public.accounts VALUES (3, 'user@gmail.com', 'User', '$2a$10$PGWSCwHGRv8wlxNwfQ1DE.LH8odMOx52q/GmT.Ue2/xAMfU0VtJXG', 3);
INSERT INTO public.accounts VALUES (4, 'staff@gmail.com', 'Staff', '$2a$10$PGWSCwHGRv8wlxNwfQ1DE.LH8odMOx52q/GmT.Ue2/xAMfU0VtJXG', 4);


--
-- Data for Name: categories; Type: TABLE DATA; Schema: public; Owner: admin
--

INSERT INTO public.categories VALUES (1, 'Orchids');
INSERT INTO public.categories VALUES (2, 'Exotic Flowers');
INSERT INTO public.categories VALUES (3, 'Indoor Plants');


--
-- Data for Name: flyway_schema_history; Type: TABLE DATA; Schema: public; Owner: admin
--



--
-- Data for Name: orchids; Type: TABLE DATA; Schema: public; Owner: admin
--

INSERT INTO public.orchids VALUES (1, 'Natural orchid', true, 'Phalaenopsis', 10, 'https://images.unsplash.com/photo-1610397648930-477b8c7f0943?q=80&w=730&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 1);
INSERT INTO public.orchids VALUES (2, 'Hybrid orchid', false, 'Cattleya', 15, 'https://plus.unsplash.com/premium_photo-1673931249523-69dcbace086b?q=80&w=687&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 2);
INSERT INTO public.orchids VALUES (3, 'Random orchid', true, 'Dendrobium', 20, 'https://images.unsplash.com/photo-1562133558-4a3906179c67?q=80&w=735&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D', 3);


--
-- Data for Name: orders; Type: TABLE DATA; Schema: public; Owner: admin
--

INSERT INTO public.orders VALUES (1, '2025-06-29 22:22:16.866', 0, 99.9, 3);
INSERT INTO public.orders VALUES (2, '2025-06-29 22:22:16.866', 1, 149.5, 3);
INSERT INTO public.orders VALUES (3, '2025-06-29 22:22:16.866', 2, 199, 3);


--
-- Data for Name: order_details; Type: TABLE DATA; Schema: public; Owner: admin
--

INSERT INTO public.order_details VALUES (1, 10, 2, 1, 1);
INSERT INTO public.order_details VALUES (2, 15, 1, 2, 2);
INSERT INTO public.order_details VALUES (3, 20, 3, 3, 3);


--
-- Data for Name: tokens; Type: TABLE DATA; Schema: public; Owner: admin
--



--
-- Name: accounts_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.accounts_id_seq', 4, true);


--
-- Name: categories_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.categories_id_seq', 3, true);


--
-- Name: orchids_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.orchids_id_seq', 3, true);


--
-- Name: order_details_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.order_details_id_seq', 3, true);


--
-- Name: orders_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.orders_id_seq', 3, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.roles_id_seq', 4, true);


--
-- Name: tokens_id_seq; Type: SEQUENCE SET; Schema: public; Owner: admin
--

SELECT pg_catalog.setval('public.tokens_id_seq', 1, false);


--
-- PostgreSQL database dump complete
--

