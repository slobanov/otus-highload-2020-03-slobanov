window.onload = () => {
    let isError = new URLSearchParams(window.location.search).has("error");
    if (isError) {       
        $("main").prepend(errorBar("Incorrect username or password"))
    }
};
