"use client"
import { useSearchParams } from "next/navigation";
import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import Link from 'next/link';
import {GetResMenuOrderDto,PutMenuOrderRequestDTO,RsData } from "./types";

export function mapOrdersToPutMenuOrderRequest(order: GetResMenuOrderDto): PutMenuOrderRequestDTO {
    return {
        coffeeOrders: order.orders.map((detail) => ({
            id: detail.id, // GetResDetailOrderDto의 id를 BeanIdQuantityDTO의 id로 매핑
            quantity: detail.quantity, // GetResDetailOrderDto의 quantity를 BeanIdQuantityDTO의 quantity로 매핑
        })),
    };
}

export async function ModifyOrder(id: number, updatedOrder: PutMenuOrderRequestDTO): Promise<RsData> {
    const url = new URL(`/api/order/${id}`, window.location.origin);

    const response = await fetch(url.toString(), {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedOrder), // 🔹 수정된 주문 정보 전송
    });


    const resData = await response.json();
    console.log("modifyOrder");
    console.log("------------------------------------------------");
    console.log("data:\n", JSON.stringify(resData, null, 2));
    console.log("------------------------------------------------");

    return resData;
}

export default function Page() {
    const router = useRouter();
    const [order, setOrder] = useState<GetResMenuOrderDto | null>(null);
    const [modifyOrder, setModifyOrder] = useState<PutMenuOrderRequestDTO | null>(null);

    // sessionStorage에서 데이터 불러오기
    useEffect(() => {
        const storedOrder = sessionStorage.getItem("selectedOrder");
        if (storedOrder) {
            setOrder(JSON.parse(storedOrder)); // JSON 문자열을 객체로 변환
        }

    }, []);

    if (!order) {
        return <p className="text-center text-gray-500">📦 주문 정보를 불러오는 중...</p>;
    }

    const increaseQuantity = (id: number) => {
        if (!order) return;
        const updatedOrders = order.orders.map((detail) =>
            detail.id === id ? { ...detail, quantity: detail.quantity + 1 } : detail
        );
        setOrder({ ...order, orders: updatedOrders });
    };

    // 🔽 수량 감소 함수
    const decreaseQuantity = (id: number) => {
        if (!order) return;
        const updatedOrders = order.orders.map((detail) =>
            detail.id === id && detail.quantity >= 1 ? { ...detail, quantity: detail.quantity - 1 } : detail
        );
        setOrder({ ...order, orders: updatedOrders });
    };

    return (
        <div className="container mx-auto px-4">
            <div className="text-center my-8">
                    <h1 className="text-2xl font-bold cursor-pointer"
                        onClick={() => router.push("/")}>Grids & Circle</h1>
            </div>

            <div className="bg-white shadow-lg rounded-lg p-6">
                <h2 className="text-xl font-bold mb-4">주문 정보</h2>
                <p className="text-gray-600">ID: {order.id}</p>
                <p className="text-gray-600">이메일: {order.email}</p>
                <p className="text-gray-600">주소: {order.address}</p>
                <p className="text-gray-600">우편번호: {order.postCode}</p>

                <h3 className="text-lg font-semibold mt-6">주문 상세</h3>
                <ul className="space-y-4">
                {order.orders.length > 0 ? (
                order.orders.map((detail) => (
                <li key={detail.id} className="flex items-center bg-gray-100 p-4 rounded-lg">

                

                {/* 📌 상품 이름 */}
                <div className="flex-1 ml-4">
                    <div className="text-gray-500">주문 상품</div>
                    <div className="font-semibold">{detail.name}</div>
                </div>

                {/* 📦 수량 조절 영역 */}
                <div className="flex items-center space-x-2 ml-4">
                    {/* 🔽 수량 감소 버튼 */}
                    <button
                        onClick={() => decreaseQuantity(detail.id)}
                        className="bg-gray-300 text-gray-700 px-3 py-2 rounded"
                    >
                        -
                    </button>

                    <span className="w-10 text-center font-semibold">{detail.quantity}개</span>

                    {/* 🔼 수량 증가 버튼 */}
                    <button
                        onClick={() => increaseQuantity(detail.id)}
                        className="bg-gray-300 text-gray-700 px-3 py-2 rounded"
                    >
                        +
                    </button>
                    </div>
                </li>
                ))
                ) : (
                <li className="text-gray-500 text-center">📭 주문 상세 없음</li>
                )}
                </ul>
            </div>

            <div className="mt-6 flex justify-center">
                <button
                onClick={async () => {
                if (!order) return;
                const updatedOrder = mapOrdersToPutMenuOrderRequest(order); // 변환
                const data:RsData= await ModifyOrder(order.id, updatedOrder); // PUT 요청
                if (data.resultCode === "200-1") {
                    alert("주문내역을 수정하였습니다");
                    window.location.reload();
                } else {
                    alert("주문내역 수정을 실패했습니다.");
                }
                router.push(`/order/history?page=0&pageSize=10&email=${encodeURIComponent(order.email)}`);
                }}
                className="bg-black text-white px-6 py-3 rounded-lg font-semibold transition hover:bg-gray-800"
                >
                수정하기
                </button>
            </div>
        </div>
    );
}