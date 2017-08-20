class RobbyContainer extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            roomInfo: null
        };

        this.getRooms();

    }

    getRooms() {

        let root = 'http://localhost:9000';
        console.log("getRooms before ajax");

        $.get([
            root + "/robby/rooms",
        ]).done(function(data) {
            this.setState({roomInfo: data});
        }.bind(this));
    };


    render() {
        console.log("render");

        if(this.state.roomInfo) {
            return <h1>Robby + {this.state.roomInfo[0].roomId}</h1>;
        } else {
            return <h1>"loading"</h1>;
        }

    }
}


ReactDOM.render(
    <RobbyContainer />,
    document.getElementById('root')
);