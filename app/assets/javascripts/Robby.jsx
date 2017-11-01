class RobbyContainer extends React.Component {


    constructor(props) {
        super(props);
        this.state = {
            roomInfo: null,
            roomName: ""
        };

        this.handleRoomNameChange = this.handleRoomNameChange.bind(this);
        this.makeRoom = this.makeRoom.bind(this);
        this.showRooms = this.showRooms.bind(this);

        this.getRooms();

        this.url = 'http://localhost:9000';
    }


    handleRoomNameChange(e) {
        this.setState({roomName: e.target.value})
    }

    getRooms() {

        // 이걸 클래스 변수로 바꾸려면 어떻게 해야하는지 모르겠음
        let root = 'http://localhost:9000';
        console.log("getRooms before ajax");

        $.get([
            root + "/robby/rooms",
        ]).done(function(data) {
            this.setState({roomInfo: data});
        }.bind(this));
    };

    makeRoom(event) {
        console.log("makeRoom");
        let root = 'http://localhost:9000';

        console.log("roomName : ", this.state.roomName);

        $.post(root + "/robby",
            {
                roomName: this.state.roomName
            }).done(function(data) {
            console.log('done post');
            console.log(data);
        });

        this.getRooms();

        event.preventDefault();
    }

    showRooms() {

        console.log("showRooms");

        if(this.state.roomInfo !== null) {

            // this.state.roomInfo.map((room, index) => console.log(room));
            // this.state.roomInfo.map((room, index) => console.log(room.roomName));

            return (
                this.state.roomInfo.map(
                    (room, index) => (
                        <h1 key={room.id}>{room.id}  {room.name}</h1>
                    )
                )
            )

        } else {
            return <h1>"loading"</h1>;
        }

    }


    render() {

        return (
            <form onSubmit={this.makeRoom}>
                <label>
                    Room name :
                    <input type="text" value={this.state.roomName} onChange={this.handleRoomNameChange}/>
                </label>
                <input type="submit" value="submit"/>

                {this.showRooms()}
            </form>

        );

    }

}


ReactDOM.render(
    <RobbyContainer />,
    document.getElementById('root')
);