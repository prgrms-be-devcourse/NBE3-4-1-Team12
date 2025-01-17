"use client"
import { login } from "./api";
import { useRouter } from "next/navigation";

export default async function Page() {
    const router = useRouter();
    return(
        <div className="container mx-auto px-4">
            {/* 사이트 이름름 */}
            <div className="text-center my-8">
                <h1 className="text-2xl font-bold" role="button" 
                onClick={() => router.push("/")} >Grids & Circle</h1>
            </div>
            <div className="bg-white shadow-lg rounded-lg">
            
                <div className="grid grid-cols-1 md:grid-cols-3"> 이메일 입력
                </div>
            </div>
        </div>
    );
}