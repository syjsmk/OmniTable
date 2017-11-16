class Room extends React.Component {

    constructor(props) {
        super(props);

        // 선택한 room의 id는 room.scala.html에서 바인딩된 값을 사용함
        this.state = {
            url: 'http://localhost:9000',
            id: roomId
        };

        this.getRoom = this.getRoom.bind(this);

        this.getRoom();
    }


    getRoom() {
        console.log("getRoom");
        console.log(this.state.id);

        $.get(
            this.state.url + "/room/data/" + this.state.id
        ).done(function (data) {
            console.log(data);
        });

    }

    render() {
        return (
            <h1>room</h1>
        )
    }
}

ReactDOM.render(
    <Room/>,
    document.getElementById('root')
);
