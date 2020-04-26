window.onload = () => {
    let searchParams = new URLSearchParams(window.location.search);
    if (searchParams.has("limit")) {
        limit = parseInt(searchParams.get("limit"));
    }
    if (searchParams.has("offset")) {
        offset = parseInt(searchParams.get("offset"));
    }
    handle_light_users();
};

next_users = []
limit = 10;
offset = 0;

handle_light_users = () => {
    if (offset <= 0) {
        $("button#prev").prop("disabled", true);
    } else if (offset >= limit) {
        $("button#prev").prop("disabled", false);
    }
    $.get(`${api}/user?limit=${limit+1}&offset=${offset}`).then(
        light_users => {
            $("tbody#users").empty();
            if (light_users.length <= limit) {
               $("button#next").prop("disabled", true);
            } else {
               $("button#next").prop("disabled", false);
               light_users.pop();
            }
            light_users.forEach(display_light_user)
        }
    );
};
display_light_user = (light_user) => $("tbody#users").append(light_user_string(light_user));
light_user_string = (light_user) => `
    <tr>
        <td>${user_link(light_user.login)}</td>
        <td>${light_user.firstName}</td>
        <td>${light_user.lastName}</td>
    </tr>
`;

next = () => {
    offset += limit;
    handle_light_users();
};

previous = () => {
    offset -= limit;
    handle_light_users();
};
