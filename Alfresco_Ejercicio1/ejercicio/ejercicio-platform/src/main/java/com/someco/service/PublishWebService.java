package com.someco.service;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.alfresco.service.namespace.QName;
import org.alfresco.service.cmr.workflow.WorkflowPath;

/**
 * Servicio para gestionar publicación/despublicación de documentos a web
 */
@Service("publishWebService")
public class PublishWebService {

    @Autowired
    @Qualifier("WorkflowService")
    private WorkflowService workflowService;

    public void publishDocument(NodeRef nodeRef) {
        String currentUser = AuthenticationUtil.getFullyAuthenticatedUser();
        startWorkflow("publishWeb", nodeRef, currentUser);
    }

    public void unpublishDocument(NodeRef nodeRef) {
        String currentUser = AuthenticationUtil.getFullyAuthenticatedUser();
        startWorkflow("unpublishWeb", nodeRef, currentUser);
    }

    private void startWorkflow(String workflowId, NodeRef nodeRef, String initiator) {
        try {
            String fullWorkflowId = "activiti$" + workflowId;
            WorkflowDefinition workflowDef = workflowService.getDefinitionByName(fullWorkflowId);

            if (workflowDef == null) {
                throw new RuntimeException("Workflow '" + fullWorkflowId + "' no encontrado");
            }

            Map<QName, Serializable> variables = new HashMap<>();
            variables.put(QName.createQName("http://www.alfresco.org/model/bpm/1.0", "assignee"), initiator);
            variables.put(QName.createQName("http://www.alfresco.org/model/content/1.0", "nodeRef"), nodeRef.toString());

            workflowService.startWorkflow(workflowDef.getId(), variables);

        } catch (Exception e) {
            throw new RuntimeException("Error iniciando workflow: " + e.getMessage(), e);
        }
    }
}