package com.someco.service;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.workflow.WorkflowService;
import org.alfresco.service.cmr.workflow.WorkflowDefinition;
import org.alfresco.service.cmr.workflow.WorkflowInstance;
import org.alfresco.repo.security.authentication.AuthenticationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para gestionar publicación/despublicación de documentos a web
 */
@Service("publishWebService")
public class PublishWebService {

    @Autowired
    private WorkflowService workflowService;

    /**
     * Inicia el workflow de publicación web
     * @param nodeRef Referencia del documento
     */
    public void publishDocument(NodeRef nodeRef) {
        String currentUser = AuthenticationUtil.getFullyAuthenticatedUser();
        startWorkflow("publishWeb", nodeRef, currentUser);
    }

    /**
     * Inicia el workflow de despublicación web
     * @param nodeRef Referencia del documento
     */
    public void unpublishDocument(NodeRef nodeRef) {
        String currentUser = AuthenticationUtil.getFullyAuthenticatedUser();
        startWorkflow("unpublishWeb", nodeRef, currentUser);
    }

    /**
     * Inicia un workflow con los parámetros necesarios
     */
    private void startWorkflow(String workflowId, NodeRef nodeRef, String initiator) {
        try {
            // Obtener la definición del workflow
            WorkflowDefinition workflowDef = workflowService.getDefinitionByName(workflowId);

            if (workflowDef == null) {
                throw new RuntimeException("Workflow '" + workflowId + "' no encontrado");
            }

            // Preparar variables del workflow
            Map<String, Serializable> variables = new HashMap<>();
            variables.put("bpm:assignee", initiator);
            variables.put("nodeRef", nodeRef.toString());

            // Iniciar instancia del workflow
            WorkflowInstance workflowInstance = workflowService.startWorkflow(workflowDef.getId(), variables);

            System.out.println("Workflow '" + workflowId + "' iniciado con ID: " + workflowInstance.getId());

        } catch (Exception e) {
            throw new RuntimeException("Error iniciando workflow '" + workflowId + "': " + e.getMessage(), e);
        }
    }
}