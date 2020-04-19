window.onload = () => {
    $.get(`${api}/interest/user/${entity_id()}`).then(display_interests);
    $.get(`${api}/user/${entity_id()}`).then(display_personal);
};

display_personal = (user) => {
    if (user) {
        $("td#first-name").text(user.firstName)
        $("td#last-name").text(user.lastName)
        $("td#age").text(user.age)
        $("td#gender").text(user.gender)
        $("td#city").text(user.city)
    } else {
        window.location.replace(`${context}/error404`)
    }
};

display_interests = (interests) => {
    if (interests) {
        let td_interests = $("td#interests");
        td_interests.append(interests.map(interest_string).join("\n"));
    }
};

interest_string = interest => `<div>${interest}</div>`;
