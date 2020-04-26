window.onload = () => {
    $.get(`${api}/gender/`).then(fill_gender_options);
    $.get(`${api}/city/`).then(fill_city_options);
    $.get(`${api}/interest/`).then(fill_interest_options);

    $("form#sign-up").attr("action", "javascript:sign_up()");
};

fill_list_options = (list_id, options) => {
    let list = $(`#${list_id}`);
    options.forEach(value => list.append(`<option>${value}</option`));
};

fill_gender_options = (genders) => fill_list_options("gender", genders);
fill_city_options = (cities) => fill_list_options("city", cities);
fill_interest_options = (interests) => fill_list_options("interests", interests);

let interests = new Set();

add_interest = () => {
    let interest = $("#interest-input").val();
    if (interest && !interests.has(interest)) {
        $("div#selected-interests").append(
            `<button type="button"
                    id="interest-${interest}"
                    onclick="remove_interest('${interest}')"
                    class="btn">
                ${interest}
            </button>`
        );
        interests.add(interest);
        $("#interest-input").val("");
    }
};

remove_interest = (interest) => {
    interests.delete(interest);
    $(`button#interest-${interest}`).remove();
};

sign_up = () => {
    let login = $("#username").val();
    let first_name = $("#first-name").val();
    let last_name = $("#last-name").val();
    let gender = $("#gender").val();
    let birthDate = $("#birth-date").val();
    let city = $("#city-input").val();
    let last_interest = $("#interest-input").val();
    interests.add(last_interest);
    let password = $("#password").val();

    $.ajax({
        url: `${api}/sign-up`,
        type: "POST",
        data: JSON.stringify(
            {
                "login": login,
                "firstName": first_name,
                "lastName": last_name,
                "gender": gender,
                "birthDate": birthDate,
                "city": city,
                "interests": Array.from(interests),
                "password": password
            }),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: (user) => {
            data = new FormData()
            data.append("username", login);
            data.append("password", password);
            data.append("remember-me", true);
            $.ajax({
                url: `${context}/login`,
                data: data,
                processData: false,
                contentType: false,
                type: 'POST',
                success: () => {
                    window.location.href = `${context}/user/${user.login}`;
                }
            })
        },
        error: (result) => {
            if (result.status == 409) {
                $("main").prepend(errorBar(`Username '${login}' already occupied`));
            } else {
                $("main").prepend(errorBar("Something went terribly wrong"));
            }
        }
    });
};
