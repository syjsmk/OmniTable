class RobbyContainer extends React.Component {

    constructor(props) {
        super(props);
        this.roomInfo = '';
    }

    async getRooms() {

        var roomInfo = '';
        var root = 'http://localhost:9000';


        roomInfo = await Promise.all([
            $.get(root + "/robby/rooms",
                function(data) {
                    roomInfo = data;
                }
            )
        ]);

        console.log("getRooms");
        console.log(roomInfo);
        return roomInfo;
    };

    componentDidMount() {
        this.roomInfo = this.getRooms();
        console.log("componentDidMount");
        console.log(this.roomInfo);
    }



    render() {
        this.roomInfo = this.getRooms();
        return <h1>Robby + {"kjljkl" + this.roomInfo}</h1>;
    }
}

ReactDOM.render(
    <RobbyContainer />,
    document.getElementById('root')
);