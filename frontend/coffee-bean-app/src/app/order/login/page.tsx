"use client"
import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";



export async function login(email: string) {
    const url = new URL("/api/order/login", window.location.origin);
    url.searchParams.append("email", email);
  
    const response = await fetch(url.toString(), {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    });
    
    const resData = await response.json();
    console.log("data:\n", JSON.stringify(resData, null, 2));
    return resData;
  }

export default function Page() {
    const router = useRouter();
    const [email, setEmail] = useState("");

    const handleConfirm =  async () => {

        if (email.trim() === "") {
            alert("이메일을 입력하세요.");
            return;
        }
        
        
        const response = await login(email);
        
        
        if(response.status==400) {
            alert("이메일 형식이 맞지 않습니다.");
            return;
        }
        
        const resultCode=response.resultCode;
        const statusCode = parseInt(resultCode.split("-")[0],10);
        
        if(statusCode==200) {
            router.push(`/order/history?page=0&pageSize=10&email=${encodeURIComponent(email)}`);
            return;
        }
        alert("에러 상황");
        
    };

    return(
        <div className="container mx-auto px-4">
            {/* 사이트 이름름 */}
            <div className="text-center my-8">
                <h1 className="text-2xl font-bold" role="button" 
                onClick={() => router.push("/")} >Grids & Circle</h1>
            </div>

            <div className="bg-white shadow-lg rounded-lg p-6 flex justify-center ">
                {/* 이메일 입력 섹션 */}
                <div className="flex flex-col gap-4 w-full max-w-sm ">
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        placeholder="이메일 입력"
                        className="border p-2 rounded w-full"
                    />
                    <button
                        onClick={handleConfirm}
                        className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
                    >
                        확인
                    </button>
                </div>
            </div>
        </div>
    );
}