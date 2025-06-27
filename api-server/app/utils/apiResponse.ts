export function successResponse<T>(data: T, message = 'Success') {
    return {
        success: true as const,
        message,
        data,
    };
}

export function errorResponse(message: string, code = 'ERROR', details?: unknown) {
    return {
        success: false as const,
        message,
        error: {
            code,
            details,
        },
    };
}
