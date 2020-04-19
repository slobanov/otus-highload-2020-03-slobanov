entity_id = () => $(location).attr("href").split("/").slice(-1).pop();
context = "/social-network";
api = `${context}/api`;

errorBar = (text) => `
    <div class="col-4 error-bar">${text}</div>
`;
