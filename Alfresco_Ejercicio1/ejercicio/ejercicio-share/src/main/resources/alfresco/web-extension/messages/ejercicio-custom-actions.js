// Namespace
if (typeof Ejercicio == "undefined" || !Ejercicio) {
  var Ejercicio = {};
}

Ejercicio.CustomActions = {};

/**
 * Start Publish Web Workflow
 */
Ejercicio.CustomActions.onActionPublishWebWorkflow = function(record) {
  var nodeRef = record.nodeRef;

  // Diálogo para iniciar workflow
  var url = Alfresco.constants.URL_SERVICECONTEXT +
            "components/workflow/start-workflow?nodeRef=" + nodeRef +
            "&workflowId=publishWeb";

  Alfresco.module.SimpleDialog.setOptions({
    width: 900,
    height: 700,
    hideIcon: true,
    draggable: true,
    resizable: true,
    modal: true,
    buttons: [
      {
        text: Alfresco.util.message("button.submit"),
        handler: function() {
          Alfresco.util.navigateTo("document-details?nodeRef=" + nodeRef);
        }
      },
      {
        text: Alfresco.util.message("button.cancel"),
        handler: function() {
          this.cancel();
        },
        isDefault: true
      }
    ]
  }).show({
    title: "Publish to Web",
    url: url
  });
};

/**
 * Start Unpublish Web Workflow
 */
Ejercicio.CustomActions.onActionUnpublishWebWorkflow = function(record) {
  var nodeRef = record.nodeRef;

  var url = Alfresco.constants.URL_SERVICECONTEXT +
            "components/workflow/start-workflow?nodeRef=" + nodeRef +
            "&workflowId=unpublishWeb";

  Alfresco.module.SimpleDialog.setOptions({
    width: 900,
    height: 700,
    hideIcon: true,
    draggable: true,
    resizable: true,
    modal: true,
    buttons: [
      {
        text: Alfresco.util.message("button.submit"),
        handler: function() {
          Alfresco.util.navigateTo("document-details?nodeRef=" + nodeRef);
        }
      },
      {
        text: Alfresco.util.message("button.cancel"),
        handler: function() {
          this.cancel();
        },
        isDefault: true
      }
    ]
  }).show({
    title: "Unpublish from Web",
    url: url
  });
};