"use client"
import { useSearchParams } from "next/navigation";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from 'next/link';
import { RsData, PageDto, GetResMenuOrderDto } from "./types";

export async function GetList(page: number, pageSize: number ,email: string): Promise<RsData> {


    const url = new URL("/api/order/history", window.location.origin);
    url.searchParams.append("email", email);
    url.searchParams.append("page", page.toString());
    url.searchParams.append("pageSize", pageSize.toString());
  
    const response = await fetch(url.toString(), {
      method: "GET",
      headers: { "Content-Type": "application/json" },
    });
    
    const resData = await response.json();
    console.log("GetList")
    /*
    console.log("------------------------------------------------")
    console.log("data:\n", JSON.stringify(resData, null, 2));
    console.log("------------------------------------------------")
    */
    return resData;
  }

  export async function DeleteOrder(id: number): Promise<RsData> {
    const url = new URL(`/api/order/${id}`, window.location.origin);

    const response = await fetch(url.toString(), {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
    });

    const resData = await response.json();
    console.log("deleteOrder");
    console.log("------------------------------------------------");
    console.log("data:\n", JSON.stringify(resData, null, 2));
    console.log("------------------------------------------------");

    return resData;
}

export default function Page() {

    const defaultPageDto: PageDto = {
        currentPageNumber: 0,
        pageSize: 10,
        totalPages: 0,
        totalItems: 0,
        items: []
    };

    const searchParams = useSearchParams();
    const router = useRouter();

    const page: number = parseInt(searchParams.get("page") || "0", 10);
    const pageSize: number = parseInt(searchParams.get("pageSize") || "10", 10);
    const email: string = searchParams.get("email") || "";

    const [responseBody, setResponseBody] = useState<RsData | null>(null); // 서버 데이터를 저장할 상태
    
    //페이징 데이터 로드
    useEffect(() => {
        if (email) {
            GetList(page, pageSize, email)
                .then((res) => setResponseBody(res))
                .catch((error) => console.error("데이터 불러오기 실패:", error));
        }
        
    }, [page, pageSize, email]);


    const pageDto: PageDto = responseBody?.data ?? defaultPageDto;

    const [openOrders, setOpenOrders] = useState<{ [key: number]: boolean }>({});

    // 특정 주문 ID의 상세 내역을 토글하는 함수
    const toggleOrderDetails = (orderId: number) => {
        setOpenOrders((prev) => ({
            ...prev,
            [orderId]: !prev[orderId], // 현재 상태의 반대로 변경
        }));
    };

    const handleDelete = async (orderId: number) => {
        if (confirm(`${orderId}번 주문을 삭제하시겠습니까?`)) {
            const response = await DeleteOrder(orderId);
            if (response.resultCode === "200-1") {
                alert("삭제 성공 하였습니다");
                window.location.reload();
            } else {
                alert("삭제 실패");
            }
        }
    };

    const handleModify = (order : GetResMenuOrderDto ) => {
        sessionStorage.setItem("selectedOrder", JSON.stringify(order)); // order 객체를 문자열로 변환 후 저장
        router.push("/order/modifyPage"); // 페이지 이동
    };
      

    return (
        
            <div className="container mx-auto px-4">
                {/* 사이트 이름 */}
                <div className="text-center my-8">
                    <h1 className="text-2xl font-bold cursor-pointer"
                        onClick={() => router.push("/")}>Grids & Circle</h1>
                </div>
    
                {/* 주문 내역 섹션 */}
                <div className="bg-white shadow-lg rounded-lg p-6">
                    <h2 className="text-xl font-bold mb-4">주문 내역</h2>
    
                    {pageDto.items.length === 0 ? (
                        <p className="text-gray-600">📭 주문 내역이 없습니다.</p>
                    ) : (
                        <div className="grid grid-cols-1 gap-6">
                            {pageDto.items.map((order, index) => (
                                <div key={order.id} className="border p-4 rounded-lg shadow-sm bg-gray-50">
                                    {/* 주문 기본 정보 */}
                                    <div className="flex justify-between items-center">
                                        <h3 className="font-semibold text-lg"> 주문 {index + 1}</h3>
                                        {/* 토글 버튼 (⬇️⬆️) */}
                                        <button 
                                            onClick={() => toggleOrderDetails(order.id)}
                                            className={`px-3 py-1 rounded transition 
                                                 "bg-white text-black border"
                                            }`}
                                        >
                                            {openOrders[order.id] ? "▲" : "▼"}
                                        </button>
                                    </div>
                                    {/*
                                    <p className="text-sm text-gray-600"> 생성일: {order.createDate}</p>
                                    <p className="text-sm text-gray-600"> 수정일: {order.modifyDate}</p>
                                    */}
                                    <p className="text-sm text-gray-600"> id: {order.id}</p>
                                    <p className="text-sm text-gray-600"> 이메일: {order.email}</p>
                                    <p className="text-sm text-gray-600"> 주소: {order.address}</p>
                                    <p className="text-sm text-gray-600"> 우편번호: {order.postCode}</p>

                                    {/* 버튼 그룹 */}
                                    <div className="flex gap-2 mt-2">
                                    {/* 삭제 버튼 */}
                                        <button
                                        onClick={() => handleDelete(order.id)}
                                        className="bg-white text-black border border-black px-3 py-1 rounded-md font-bold transition hover:bg-gray-100"
                                        >
                                        삭제
                                        </button>

                                    {/* 수정 버튼 */}
                                        <button
                                        onClick={() => handleModify(order)}
                                        className="bg-white text-black border border-black px-3 py-1 rounded-md font-bold transition hover:bg-gray-100"
                                        >
                                        수정
                                        </button>
                                    </div>
    
                                    {/* 주문 상세 - 토글 방식 */}
                                    {openOrders[order.id] && (
                                        <div className="mt-4">
                                            <h4 className="font-semibold text-md"> 주문 상세</h4>
                                            <ul className="space-y-2 mt-2">
                                                {order.orders.length > 0 ? (
                                                    order.orders.map((detail) => (
                                                        <li key={detail.id} className="flex justify-between bg-white p-2 border rounded-lg">
                                                            <span> {detail.name}</span>
                                                            <span className="font-semibold"> {detail.quantity}개</span>
                                                        </li>
                                                    ))
                                                ) : (
                                                    <li className="text-gray-500"> 주문 상세 없음</li>
                                                )}
                                            </ul>
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}
    
                    {/* 페이지네이션 */}
                    <div className="flex justify-center mt-6">
                        {Array.from({ length: pageDto.totalPages }, (_, i) => i + 1).map((pageNum) => (
                            <Link
                                key={pageNum}
                                className={`px-4 py-2 mx-1 border rounded-lg ${
                                    pageNum === pageDto.currentPageNumber ? "bg-black text-white" : "bg-gray-200"
                                }`}
                                href={`?page=${pageNum - 1}&pageSize=${pageSize}&email=${email}`}
                            >
                                {pageNum}
                            </Link>
                        ))}
                    </div>
                </div>
            </div>
        );
    
    
}