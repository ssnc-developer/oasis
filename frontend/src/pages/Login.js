import React from 'react';
import {
    Badge,
    Button,
    Card,
    CardBody,
    CardFooter,
    CardHeader,
    Col,
    Container,
    Form,
    Input,
    InputGroup,
    InputGroupAddon,
    InputGroupText,
    Row,
} from 'reactstrap';
import { inject, observer } from 'mobx-react';
import logo from '../assets/img/logo_b.png';
import Loader from '../components/common/Loader';

@inject('signInStore')
@observer
class Login extends React.Component {
    componentDidMount() {
        const { signInStore, onUpdate } = this.props;
        signInStore.loginName = '';
        signInStore.password = '';
        document.title = '아임히어-Work. Web Admin - 로그인';
        onUpdate();
        document.body.classList.toggle('login-page');
        localStorage.clear();
        signInStore.root.setAuthToken();
    }

    componentWillUnmount() {
        document.body.classList.toggle('login-page');
    }

    login = async () => {
        const { history, signInStore } = this.props;
        try {
            await signInStore.signIn();
            if (localStorage.getItem('jwtToken')) {
                // signInStore.root.setAuthToken();
                await signInStore.root.myPageStore.identities();
                await signInStore.root.companyStore.preferences();
                localStorage.setItem('userName', signInStore.root.myPageStore.name);
                localStorage.setItem('userRole', signInStore.root.myPageStore.role);
                localStorage.setItem('featureEnabled', JSON.stringify(signInStore.root.companyStore.featureEnabled));
                if (signInStore.root.myPageStore.role === 'STAFF') {
                    history.push({
                        pathname: '/admin/commute/staff/',
                    });
                } else {
                    history.push({
                        pathname: '/admin/dashboard',
                    });
                }
            }
        } catch (e) {
            signInStore.root.isLoading = false;
            signInStore.handleErrorMessage('아이디와 비밀번호를 확인하세요.');
        }
    };

    keyPressInputId = e => {
        if (e.key === 'Enter') {
            const { form } = e.target;
            const index = Array.prototype.indexOf.call(form, e.target);
            form.elements[index + 1].focus();
            e.preventDefault();
        }
    };

    keyPressPassword = e => {
        if (e.key === 'Enter') {
            this.login();
        }
    };

    render() {
        const { signInStore } = this.props;

        const loginForm = {
            width: "536px"
        };

        const loginInputGroup = {
            paddingBottom: "20px"
        };

        const loginInput = {
            height: "60px",
            borderRadius: "4px",
            boxShadow: "0px 2px 2px 0 rgba(76, 81, 83, 0.05)",
            border: "solid 1px #e0e0e0",

            fontFamily: "NotoSansCJKkr",
            fontSize: "22px",
            fontWeight: "normal",
            fontStretch: "normal",
            fontStyle: "normal",
            letterSpacing: "-0.44px",
            color: "#000000"
        };

        const loginBtnFooter = {
            padding: "22px 15px"
        };

        const loginBtn = {
            margin: "0px",
            height: "80px",
            borderRadius: "4px",
            backgroundColor: "#1e7fba",

            fontFamily: "NotoSansCJKkr",
            fontSize: "26px",
            fontWeight: "normal",
            fontStretch: "normal",
            fontStyle: "normal",
            letterSpacing: "-0.52px",
            color: "#ffffff"
        };

        return (
            <div className="login-page">
                <Container>
                    <Row style={{ paddingLeft: "76px" }}>
                        <Col className="ml-auto mr-auto" lg="4" md="6">
                            <Form action="" className="form" method="" style={loginForm}>
                                <Card className="card-login">
                                    <CardHeader>
                                        <CardHeader>
                                            <div className="text-center login-header">
                                                {/*<img className="custom-logo" src={logo} alt="logo" />*/}
                                                Login
                                            </div>
                                        </CardHeader>
                                    </CardHeader>
                                    <CardBody>
                                        <InputGroup style={loginInputGroup}>
                                            {/*
                                            <InputGroupAddon addonType="prepend">
                                                <InputGroupText>
                                                    <i className="nc-icon nc-single-02 text-danger" />
                                                </InputGroupText>
                                            </InputGroupAddon>
                                            */}
                                            <Input
                                                placeholder="E-mail"
                                                type="text"
                                                autoComplete="off"
                                                name="loginName"
                                                onChange={signInStore.handleChange}
                                                onKeyPress={this.keyPressInputId}
                                                style={loginInput}
                                            />
                                        </InputGroup>
                                        <InputGroup>
                                            {/*
                                            <InputGroupAddon addonType="prepend">
                                                <InputGroupText>
                                                    <i className="nc-icon nc-key-25 text-danger" />
                                                </InputGroupText>
                                            </InputGroupAddon>
                                            */}
                                            <Input
                                                placeholder="PW"
                                                type="password"
                                                autoComplete="off"
                                                name="password"
                                                onChange={signInStore.handleChange}
                                                onKeyPress={this.keyPressPassword}
                                                style={loginInput}
                                            />
                                        </InputGroup>
                                        <Badge className="errorBadge" color="danger">
                                            {signInStore.errorMessage}
                                        </Badge>
                                    </CardBody>
                                    <CardFooter style={loginBtnFooter}>
                                        <Button block className="btn-default mb-3" color="default" style={loginBtn} onClick={this.login}>
                                            로그인
                                        </Button>
                                    </CardFooter>
                                </Card>
                            </Form>
                        </Col>
                    </Row>
                </Container>
                <div className="full-page-background" />
                {signInStore.root.isLoading && <Loader />}
            </div>
        );
    }
}

export default Login;
