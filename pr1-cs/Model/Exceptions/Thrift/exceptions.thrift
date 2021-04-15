exception SignInException {
    1: string message = "sign in error";
}

exception ServerException {
    1: string message = "server error";
}

exception DatabaseException {
    1: string message = "database error";
}

exception NetworkingException {
    1: string message = "networking error";
}

exception ParameterException {
    1: string message = "parameter error";
}

exception NotFoundException {
    1: string message = "not found error";
}

exception DuplicateException {
    1: string message = "duplicate error";
}
