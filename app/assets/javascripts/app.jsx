class App extends React.Component {
    constructor(props) {
        super(props);

        this.state = {
            roomClicked: false,
            selectedRoomId: -1
        };

        this.getCurrentPage = this.getCurrentPage.bind(this);
        this.enterRoom = this.enterRoom.bind(this);
    }

    enterRoom(e) {
        console.log(e.target.textContent);
        this.setState({
            roomClicked : true,
            selectedRoomId: e.target.textContent
        });
    }

    getCurrentPage() {
        if ( this.state.roomClicked ) {
            history.pushState(null, null, '/room/' + this.state.selectedRoomId);
            return (<Room roomId={this.state.selectedRoomId}/>);
        } else {
            return (<Robby enterRoom={this.enterRoom}/>);
        }
    }

    render() {
        const currentPage = this.getCurrentPage();

        return (
            <div>
                <TopMenu />
                {/*<Robby />*/}
                {currentPage}
            </div>
        );
    }
}

ReactDOM.render(<App />, document.getElementById('root'));