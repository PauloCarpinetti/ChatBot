# Tasks: Spec 014: Frontend Admin Panel

**Input**: Design documents from `/specs/014-admin-frontend/`

**Prerequisites**: plan.md (required), spec.md (required for user stories)

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Project initialization and basic structure

- [ ] T001 Initialize React + Vite project in `frontend-admin/`
- [ ] T002 Install Tailwind CSS, React Router, and Lucide React
- [ ] T003 Configure Tailwind CSS and PostCSS (`tailwind.config.js`, `index.css`)
- [ ] T004 Create foundational folder structure (`components/`, `layouts/`, `pages/`, `services/`, `types/`)

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core infrastructure that MUST be complete before ANY user story can be implemented

- [ ] T005 Create `AdminLayout` component (Sidebar + Top Navbar)
- [ ] T006 Configure React Router in `App.tsx` with basic routes
- [ ] T007 Create mock data services for Tenants and Chat Analytics

---

## Phase 3: User Story 1 - Admin Dashboard (Priority: P1) 🎯 MVP

**Goal**: Overview of metrics

- [ ] T008 [US1] Create Dashboard page component
- [ ] T009 [US1] Build summary Metric Cards (Total Tenants, Active Chats, API Calls)
- [ ] T010 [US1] Connect Dashboard to mock services

---

## Phase 4: User Story 2 - Tenant Management (Priority: P2)

**Goal**: View and manage tenants

- [ ] T011 [US2] Create Tenants list page with a data table
- [ ] T012 [US2] Create 'Add/Edit Tenant' form modal
- [ ] T013 [US2] Integrate Tenants page with mock CRUD service

---

## Phase 5: User Story 3 - Chat History Analytics (Priority: P3)

**Goal**: Inspect conversations

- [ ] T014 [US3] Create Chat Analytics page
- [ ] T015 [US3] Build chat transcript viewer component
- [ ] T016 [US3] Integrate Analytics page with mock chat history service

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Improvements that affect multiple user stories

- [ ] T017 Refine UI aesthetics (glassmorphism, dark mode gradients, transitions)
- [ ] T018 Check mobile responsiveness
- [ ] T019 Ensure Lighthouse accessibility standards
