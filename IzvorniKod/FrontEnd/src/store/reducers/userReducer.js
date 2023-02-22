import * as actionTypes from '../actions';

const initialState = {
    user: null
};

const reducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.USER_FETCHED:
            return {
                ...state,
                user: action.payload.user
            }
        default:
            return state;
    }
}

export default reducer;