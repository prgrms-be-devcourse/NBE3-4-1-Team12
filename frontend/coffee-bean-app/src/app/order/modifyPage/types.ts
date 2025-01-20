export interface RsData {
    resultCode: string;
    msg: string;
    data: PutMenuOrderRequestDTO;
}

export interface GetResMenuOrderDto {
    id: number;
    createDate: string; // LocalDateTime을 문자열로 변환
    modifyDate: string; // LocalDateTime을 문자열로 변환
    email: string;
    address: string;
    postCode: string;
    orders: GetResDetailOrderDto[];
}

export interface GetResDetailOrderDto {
    id: number;
    name: string; // Optional (Java에서는 기본값이 null 가능)
    quantity: number; // Optional (Java에서는 기본값이 null 가능)
}

export interface PutMenuOrderRequestDTO {
    coffeeOrders: BeanIdQuantityDTO[];
}

export interface BeanIdQuantityDTO {
    id: number; // Java에서 Long → TypeScript에서 number
    quantity: number; // Java의 Integer → TypeScript의 number
}