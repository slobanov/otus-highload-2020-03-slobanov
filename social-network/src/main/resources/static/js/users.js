let next_users = [];
let limit = 10;
let offset = 0;
let firstNamePrefix = "";
let lastNamePrefix = "";

window.onload = () => {
    let searchParams = new URLSearchParams(window.location.search);
    if (searchParams.has("limit")) {
        limit = parseInt(searchParams.get("limit"));
    }
    if (searchParams.has("offset")) {
        offset = parseInt(searchParams.get("offset"));
    }
    if (searchParams.has("firstNamePrefix")) {
        firstNamePrefix = searchParams.get("firstNamePrefix");
        $("#firstName").val(firstNamePrefix);
    }
    if (searchParams.has("lastNamePrefix")) {
        lastNamePrefix = searchParams.get("lastNamePrefix");
        $("#lastName").val(lastNamePrefix);
    }
    handle_light_users();
    $("form#search").attr("action", "javascript:set_up_search()");
};

let handle_light_users = () => {
    if (offset <= 0) {
        $("button#prev").prop("disabled", true);
    } else if (offset >= limit) {
        $("button#prev").prop("disabled", false);
    }
    $.ajax({
        url: `${api}/user`,
        data: {
            limit: limit + 1,
            offset: offset,
            firstNamePrefix: firstNamePrefix,
            lastNamePrefix: lastNamePrefix
        },
        success: (light_users) => {
            $("tbody#users").empty();
            if (light_users.length <= limit) {
                $("button#next").prop("disabled", true);
            } else {
                $("button#next").prop("disabled", false);
                light_users.pop();
            }
            light_users.forEach(display_light_user)
        }
    });
};

let change_offset = (value) => {
    offset += value
    let searchParams = new URLSearchParams(window.location.search);
    if (offset > 0) {
        searchParams.set("offset", offset)
    } else {
        searchParams.delete("offset")
    }
    update_window(searchParams);
};

let display_light_user = (light_user) => $("tbody#users").append(light_user_string(light_user));
let light_user_string = (light_user) => `
    <tr>
        <td>${user_link(light_user.login)}</td>
        <td>${light_user.firstName}</td>
        <td>${light_user.lastName}</td>
    </tr>
`;

let next = () => {
    change_offset(+limit);
    handle_light_users();
};

let previous = () => {
    change_offset(-limit);
    handle_light_users();
};

let set_up_search = () => {
    let searchParams = new URLSearchParams(window.location.search);
    firstNamePrefix = $("#firstName").val();
    lastNamePrefix = $("#lastName").val();

    if (firstNamePrefix && lastNamePrefix) {
        searchParams.set("firstNamePrefix", firstNamePrefix);
        searchParams.set("lastNamePrefix", lastNamePrefix);
    } else {
        searchParams.delete("firstNamePrefix");
        searchParams.delete("lastNamePrefix");
    }

    update_window(searchParams);
    change_offset(-offset);
    handle_light_users();
};

let update_window = (searchParams) => {
    window.history.replaceState("", "", window.location.pathname + "?" + searchParams.toString());
};

let reset_search = () => {
    firstNamePrefix = "";
    lastNamePrefix = "";

    let searchParams = new URLSearchParams(window.location.search);
    searchParams.delete("firstNamePrefix");
    searchParams.delete("lastNamePrefix");
    $("#firstName").val("")
    $("#lastName").val("")

    update_window(searchParams);
    change_offset(-offset);
    handle_light_users();
};
