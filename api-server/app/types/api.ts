export type SuccessResponse<T> = {
    success: true;
    message: string;
    data: T;
};

export type ErrorResponse = {
    success: false;
    message: string;
    error: {
        code: string;
        details?: unknown;
    };
};

export type ApiResponse<T> = SuccessResponse<T> | ErrorResponse;
