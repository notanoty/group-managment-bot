import { PrismaClient } from '@prisma/client'
import {successResponse} from "@/app/utils/apiResponse";

const prisma = new PrismaClient()

export async function GET() {
    const users = await prisma.users.findMany()

    return Response.json( successResponse(users, "List of users sent"));
}

export async function POST() {

    const user = await prisma.users.create({
        data: {
            login: "test",
            firstname: "test",
            lastname: "test",
            password: "test",
        }
    })

    return Response.json( successResponse(user, "User created successfully"));
}
