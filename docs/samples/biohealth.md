BioHealth Knowledge Profile: Vector Database Ingestion Specification

1. Executive Identity and Semantic Context

In a Retrieval-Augmented Generation (RAG) ecosystem, the definition of a clear corporate identity is a strategic imperative. This identity acts as the "Semantic North Star" for the Large Language Model (LLM), ensuring that interactions are grounded in a verified source of truth rather than generalized patterns. For BioHealth, this foundational context prevents the erosion of user trust by aligning AI responses with specific institutional protocols and contractual realities.

Primary Mission: To deliver nationwide, protocol-driven healthcare through a structured care network, ensuring immediate accessibility and clinical excellence in both elective and emergency scenarios.

Core Identity Table

Attribute	Specification
Entity Name	BioHealth
Document Source	BioHealth - Manual do Convênio
Service Category	Healthcare Provider / Health Insurance
Primary Value Proposition	National coverage and structured, protocol-based care.

Evaluation of "National Coverage" (Cobertura Nacional): This claim is a high-weight keyword that significantly impacts user expectations. Knowledge engineering must treat "National Coverage" as an accessibility guarantee. If a user queries service availability in remote or diverse regions, the RAG system must be prepared to trigger a specific "Service Scope" retrieval node to confirm provider presence, preventing the LLM from making unsupported assumptions about local network density.

2. Service Scope and Regional Coverage

The strategic value of geographic data in a vector database lies in its ability to act as a definitive boundary, preventing "hallucinations" regarding service limitations. Explicitly mapping these boundaries ensures that the LLM does not suggest out-of-network services that could lead to financial or clinical frustration for the policyholder.

* Cobertura Nacional (National Coverage): This is a primary retrieval anchor. It implies that the policyholder is protected across all domestic territories. In the embedding process, keywords such as "viagem" (travel), "outra cidade" (other city), and "rede" (network) must be weighted to trigger this node via high cosine similarity scores.

Vector DB Strategy: By identifying this scope as a "Primary Retrieval Anchor," we ensure that any query regarding geographic utility—"Onde posso usar?" (Where can I use it?)—returns an authoritative confirmation of national reach before the LLM processes more granular procedural rules.

3. Procedural Constraints: Waiting Periods (Carência)

Precision in "Waiting Period" (Carência) data is critical for legal compliance and user safety. In a health-related chatbot, miscommunicating a waiting period can result in service denial at the point of care. The RAG architecture must treat these periods as hard contractual boundaries.

Service vs. Waiting Period (Carência)

Service Type	Waiting Period (Carência)
Consultas de Emergência	Zero (Immediate)
Exames de Alta Complexidade	180 Days

The "So What?" Layer (Strategic Analysis):

* Emergency Node: The "Zero Waiting Period" for emergency consultations is a critical safety feature. The system must prioritize this information when detecting high-stress intent.
* Complex Exam Logic: The 180-day rule for "Exames de Alta Complexidade" acts as a major contractual threshold. If a user query is ambiguous regarding the type of exam, the LLM must be instructed to ask the user if the procedure qualifies as "alta complexidade" and explicitly flag the potential 180-day restriction if the account age is insufficient.

4. Emergency Protocols and Contact Intelligence

Emergency data retrieval requires "High-Precision" optimization. In scenarios involving Internação (hospitalization), the latency and accuracy of the retrieval node are paramount. This section must be isolated as a high-priority "Emergency Retrieval Node."

For all queries related to hospitalization or urgent assistance, the system must trigger the following directive:

CRITICAL CALL-TO-ACTION (CTA): Ligue para 0800-BIO-1234 em caso de internação.

Implementation Directive for the LLM: In the event of a query containing the keyword "internação", the chatbot must prioritize the 0800-BIO-1234 number above all other descriptive text. This contact should be rendered as a bolded, standalone line or a primary UI button to ensure immediate visibility.

5. Structured Data for Vector Indexing (Metadata Layer)

To ensure data isolation and security in a multi-tenant environment, all ingested chunks must adhere to strict metadata tagging. Using tenantId as a filter ensures that BioHealth data remains siloed from other insurance entities, as per the "Arquitetura Chatbot Multi-Tenant" documentation.

Suggested Metadata Structure (JSON)

{
  "tenantId": "BIOHEALTH_SECURE_001",
  "entity_name": "BioHealth",
  "source_doc": "BioHealth - Manual do Convênio",
  "category": "Health_Insurance_Policy",
  "priority_level": "High",
  "last_updated": "{{INGESTION_DATE}}",
  "geographic_scope": "National",
  "contains_emergency_info": true
}


System Prompt Constraint

The LLM must be governed by the following logic to handle procedural complexity:

"If the user asks about an 'exame' or 'exames,' the system must verify the retrieved context for 'alta complexidade' (high complexity). If this condition is met, or if the user's account age is less than 180 days, the LLM must explicitly trigger a warning regarding the 180-day waiting period (carência). Always prioritize 'internação' protocols with the 0800-BIO-1234 contact."

Conclusion

This specification transforms the BioHealth manual into a high-fidelity, searchable knowledge asset. By utilizing precise metadata filtering (tenantId), high-weight keywords (Cobertura Nacional, Internação), and strict prompt constraints, the RAG system will provide policyholders with secure, context-aware, and legally compliant assistance.
