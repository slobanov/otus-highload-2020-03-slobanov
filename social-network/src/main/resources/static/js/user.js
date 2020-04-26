window.onload = () => {
    let login = entity_id();
    $.get(`${api}/interest/user/${login}`).then(display_interests);
    $.get(`${api}/followed/user/${login}`).then(display_followed);
    $.get(`${api}/user/${login}`).then(display_personal);
    $.get(`${api}/followed/${login}`).then(canFollow => {
        if (canFollow) {
            $("button#follow").show();
        }
    });
};

display_followed = (followed) => {
    if (followed) {
        let td_follows = $("td#follows");
        td_follows.append(followed.map(user_link).join("<br>"));
    }
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

follow = () => {
    let login = entity_id();
    $.ajax({
        url: `${api}/followed/${login}`,
        type: "POST",
        success: () => window.location.href = `${context}/`
    });
}
