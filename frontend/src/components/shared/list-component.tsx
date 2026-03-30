import React from "react";

interface ListComponentProps<T> {
  data: T[];
  renderItem: (item: T, index: number) => React.ReactNode;
}

const ListComponent = <T,>({
  data = [],
  renderItem,
}: ListComponentProps<T>) => {
  return <>{data.map((item, index) => renderItem(item, index))}</>;
};

export default ListComponent;
