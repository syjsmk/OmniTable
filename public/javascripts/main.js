function getRooms() {
    $.get("/robby/rooms",
        function(data) {
            console.log(data);
        }
    );
}